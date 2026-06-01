package com.marmitaria.model;

public class PedidoMensagem {
    private Long pedidoId;
    private String status;
    private double valorTotal;
    private String cepEntrega;

    public PedidoMensagem() {}

    public PedidoMensagem(Pedido pedido) {
        this.pedidoId = pedido.getId();
        this.status = pedido.getStatus();
        this.valorTotal = pedido.getValorTotal();
        this.cepEntrega = pedido.getCepEntrega();
    }

    public Long getPedidoId() {
        return pedidoId;
    }

    public void setPedidoId(Long pedidoId) {
        this.pedidoId = pedidoId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public double getValorTotal() {
        return valorTotal;
    }

    public void setValorTotal(double valorTotal) {
        this.valorTotal = valorTotal;
    }

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }
}
