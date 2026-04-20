package com.marmitaria.repository;

import com.marmitaria.model.Pedido;
import java.util.List;

public interface PedidoRepository {
    Pedido save(Pedido pedido);
    List<Pedido> findAll();
    Pedido findById(Long id);
    void update(Pedido pedido);
    void delete(Long id);
}
