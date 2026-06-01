package com.marmitaria.controller;

import com.marmitaria.model.Pedido;
import com.marmitaria.model.ItemPedido;
import com.marmitaria.model.PedidoRequest;
import com.marmitaria.service.PedidoService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pedidos") 
public class PedidoController {

    private final PedidoService service;

    public PedidoController(PedidoService service) {
        this.service = service;
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return service.listarTodos();
    }

    @PostMapping
    public Pedido criar(@RequestBody List<ItemPedido> itens) {
        return service.criarPedido(itens);
    }

    @PostMapping("/com-entrega")
    public Pedido criarComEntrega(@RequestBody PedidoRequest request) {
        return service.criarPedidoComEntrega(request);
    }

    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @RequestBody List<ItemPedido> itens) {
        return service.atualizar(id, itens);
    }

    @PutMapping("/{id}/com-entrega")
    public Pedido atualizarComEntrega(@PathVariable Long id, @RequestBody PedidoRequest request) {
        return service.atualizarComEntrega(id, request);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @GetMapping("/{id}")
    public Pedido buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}
