package com.marmitaria.model;

import java.util.List;

public class Pedido {
    private Long id;
    private List<ItemPedido> itens;
    private double valorTotal;
    private String status;

    public Pedido(List<ItemPedido> itens) {
        this.itens = itens;
        this.valorTotal = calcularValorTotal();
        this.status = "Pendente";
    }

    public Pedido() {}

    private double calcularValorTotal() {
        return this.itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
    }

    public Long getId() {
        return id;
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
