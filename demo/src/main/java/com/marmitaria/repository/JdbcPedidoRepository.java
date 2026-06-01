package com.marmitaria.repository;

import com.marmitaria.model.ItemPedido;
import com.marmitaria.model.Marmita;
import com.marmitaria.model.Pedido;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcPedidoRepository implements PedidoRepository {

    private final DataSource dataSource;
    private final MarmitaRepository marmitaRepository;

    public JdbcPedidoRepository(DataSource dataSource, MarmitaRepository marmitaRepository) {
        this.dataSource = dataSource;
        this.marmitaRepository = marmitaRepository;
    }

    @Override
    public Pedido save(Pedido pedido) {
        String sqlPedido = "INSERT INTO pedido (valor_total, status, cep_entrega, endereco_entrega, taxa_entrega) VALUES (?, ?, ?, ?, ?)";
        String sqlItem = "INSERT INTO item_pedido (pedido_id, marmita_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmtPedido = conn.prepareStatement(sqlPedido, Statement.RETURN_GENERATED_KEYS)) {
                stmtPedido.setDouble(1, pedido.getValorTotal());
                stmtPedido.setString(2, pedido.getStatus());
                stmtPedido.setString(3, pedido.getCepEntrega());
                stmtPedido.setString(4, pedido.getEnderecoEntrega());
                stmtPedido.setDouble(5, pedido.getTaxaEntrega());
                stmtPedido.executeUpdate();

                try (ResultSet generatedKeys = stmtPedido.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        pedido.setId(generatedKeys.getLong(1));
                    }
                }
            }

            inserirItens(conn, pedido, sqlItem);

            conn.commit();
            return pedido;
        } catch (SQLException e) {
            rollback(conn);
            throw new RuntimeException("Erro ao salvar pedido", e);
        } finally {
            close(conn);
        }
    }

    @Override
    public List<Pedido> findAll() {
        List<Pedido> pedidos = new ArrayList<>();
        String sql = "SELECT * FROM pedido";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                pedidos.add(mapRowToPedido(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar pedidos", e);
        }
        return pedidos;
    }

    @Override
    public Pedido findById(Long id) {
        String sql = "SELECT * FROM pedido WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToPedido(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar pedido", e);
        }
        return null;
    }

    @Override
    public void update(Pedido pedido) {
        String sqlUpdatePedido = "UPDATE pedido SET valor_total = ?, status = ?, cep_entrega = ?, endereco_entrega = ?, taxa_entrega = ? WHERE id = ?";
        String sqlDeleteItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
        String sqlInsertItem = "INSERT INTO item_pedido (pedido_id, marmita_id, quantidade, preco_unitario) VALUES (?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlUpdatePedido)) {
                stmt.setDouble(1, pedido.getValorTotal());
                stmt.setString(2, pedido.getStatus());
                stmt.setString(3, pedido.getCepEntrega());
                stmt.setString(4, pedido.getEnderecoEntrega());
                stmt.setDouble(5, pedido.getTaxaEntrega());
                stmt.setLong(6, pedido.getId());
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteItens)) {
                stmt.setLong(1, pedido.getId());
                stmt.executeUpdate();
            }

            inserirItens(conn, pedido, sqlInsertItem);

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new RuntimeException("Erro ao atualizar pedido", e);
        } finally {
            close(conn);
        }
    }

    @Override
    public void delete(Long id) {
        String sqlDeleteItens = "DELETE FROM item_pedido WHERE pedido_id = ?";
        String sqlDeletePedido = "DELETE FROM pedido WHERE id = ?";

        Connection conn = null;
        try {
            conn = dataSource.getConnection();
            conn.setAutoCommit(false);

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeleteItens)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            try (PreparedStatement stmt = conn.prepareStatement(sqlDeletePedido)) {
                stmt.setLong(1, id);
                stmt.executeUpdate();
            }

            conn.commit();
        } catch (SQLException e) {
            rollback(conn);
            throw new RuntimeException("Erro ao deletar pedido", e);
        } finally {
            close(conn);
        }
    }

    private void inserirItens(Connection conn, Pedido pedido, String sqlItem) throws SQLException {
        try (PreparedStatement stmtItem = conn.prepareStatement(sqlItem)) {
            for (ItemPedido item : pedido.getItens()) {
                stmtItem.setLong(1, pedido.getId());
                stmtItem.setLong(2, item.getMarmita().getId());
                stmtItem.setInt(3, item.getQuantidade());
                stmtItem.setDouble(4, item.getPrecoUnitario());
                stmtItem.addBatch();
            }
            stmtItem.executeBatch();
        }
    }

    private Pedido mapRowToPedido(ResultSet rs) throws SQLException {
        Long id = rs.getLong("id");
        List<ItemPedido> itens = findItensByPedidoId(id);
        return new Pedido(
            id,
            itens,
            rs.getDouble("valor_total"),
            rs.getString("status"),
            rs.getString("cep_entrega"),
            rs.getString("endereco_entrega"),
            rs.getDouble("taxa_entrega")
        );
    }

    private List<ItemPedido> findItensByPedidoId(Long pedidoId) {
        List<ItemPedido> itens = new ArrayList<>();
        String sql = "SELECT * FROM item_pedido WHERE pedido_id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setLong(1, pedidoId);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Marmita marmita = marmitaRepository.findById(rs.getLong("marmita_id"));
                    itens.add(new ItemPedido(
                        marmita,
                        rs.getInt("quantidade"),
                        rs.getDouble("preco_unitario")
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar itens do pedido", e);
        }
        return itens;
    }

    private void rollback(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void close(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
