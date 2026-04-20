package com.marmitaria.model;

import java.util.List;

public class Pedido {
    private Long id;
    private List<ItemPedido> itens;
    private double valorTotal;
    private String status;

    public Pedido(Long id, List<ItemPedido> itens, double valorTotal, String status) {
        this.id = id;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Pedido(List<ItemPedido> itens) {
        this.itens = itens;
        this.valorTotal = calcularValorTotal();
        this.status = "Pendente";
    }

    public Pedido() {}

    private double calcularValorTotal() {
        if (this.itens == null) return 0.0;
        return this.itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
