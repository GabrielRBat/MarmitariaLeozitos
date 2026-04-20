package com.marmitaria.controller;

import com.marmitaria.model.Pedido;
import com.marmitaria.model.ItemPedido;
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

    @PutMapping("/{id}")
    public Pedido atualizar(@PathVariable Long id, @RequestBody List<ItemPedido> itens) {
        return service.atualizar(id, itens);
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