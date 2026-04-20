package com.marmitaria.repository;

import com.marmitaria.model.Marmita;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class JdbcMarmitaRepository implements MarmitaRepository {

    private final DataSource dataSource;

    public JdbcMarmitaRepository(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public Marmita save(Marmita marmita) {
        String sql = "INSERT INTO marmita (nome, preco, categoria) VALUES (?, ?, ?)";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, marmita.getNome());
            stmt.setDouble(2, marmita.getPreco());
            stmt.setString(3, marmita.getCategoria());
            
            stmt.executeUpdate();
            
            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    marmita.setId(generatedKeys.getLong(1));
                }
            }
            return marmita;
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao salvar marmita", e);
        }
    }

    @Override
    public List<Marmita> findAll() {
        List<Marmita> marmitas = new ArrayList<>();
        String sql = "SELECT * FROM marmita";
        try (Connection conn = dataSource.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            
            while (rs.next()) {
                marmitas.add(mapRowToMarmita(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao listar marmitas", e);
        }
        return marmitas;
    }

    @Override
    public Marmita findById(Long id) {
        String sql = "SELECT * FROM marmita WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return mapRowToMarmita(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao buscar marmita", e);
        }
        return null;
    }

    @Override
    public void update(Marmita marmita) {
        String sql = "UPDATE marmita SET nome = ?, preco = ?, categoria = ? WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, marmita.getNome());
            stmt.setDouble(2, marmita.getPreco());
            stmt.setString(3, marmita.getCategoria());
            stmt.setLong(4, marmita.getId());
            
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao atualizar marmita", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM marmita WHERE id = ?";
        try (Connection conn = dataSource.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Erro ao deletar marmita", e);
        }
    }

    private Marmita mapRowToMarmita(ResultSet rs) throws SQLException {
        return new Marmita(
            rs.getLong("id"),
            rs.getString("nome"),
            rs.getDouble("preco"),
            rs.getString("categoria")
        );
    }
}
