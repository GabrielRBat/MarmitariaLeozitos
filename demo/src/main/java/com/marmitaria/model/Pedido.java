package com.marmitaria.model;

import java.util.List;

public class Pedido {
    private Long id;
    private List<ItemPedido> itens;
    private double valorTotal;
    private String status;
    private String cepEntrega;
    private String enderecoEntrega;
    private double taxaEntrega;

    public Pedido(Long id, List<ItemPedido> itens, double valorTotal, String status) {
        this.id = id;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
    }

    public Pedido(Long id, List<ItemPedido> itens, double valorTotal, String status, String cepEntrega, String enderecoEntrega, double taxaEntrega) {
        this.id = id;
        this.itens = itens;
        this.valorTotal = valorTotal;
        this.status = status;
        this.cepEntrega = cepEntrega;
        this.enderecoEntrega = enderecoEntrega;
        this.taxaEntrega = taxaEntrega;
    }

    public Pedido(List<ItemPedido> itens) {
        this.itens = itens;
        this.taxaEntrega = 0.0;
        this.valorTotal = calcularValorTotal();
        this.status = "Pendente";
    }

    public Pedido() {}

    private double calcularValorTotal() {
        double subtotalItens = 0.0;
        if (this.itens != null) {
            subtotalItens = this.itens.stream().mapToDouble(ItemPedido::getSubtotal).sum();
        }
        return subtotalItens + this.taxaEntrega;
    }

    public void recalcularValorTotal() {
        this.valorTotal = calcularValorTotal();
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

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }

    public String getEnderecoEntrega() {
        return enderecoEntrega;
    }

    public void setEnderecoEntrega(String enderecoEntrega) {
        this.enderecoEntrega = enderecoEntrega;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(double taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }
}
