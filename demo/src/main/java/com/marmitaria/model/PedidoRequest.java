package com.marmitaria.model;

import java.util.List;

public class PedidoRequest {
    private List<ItemPedido> itens;
    private String cepEntrega;

    public PedidoRequest() {}

    public List<ItemPedido> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedido> itens) {
        this.itens = itens;
    }

    public String getCepEntrega() {
        return cepEntrega;
    }

    public void setCepEntrega(String cepEntrega) {
        this.cepEntrega = cepEntrega;
    }
}
