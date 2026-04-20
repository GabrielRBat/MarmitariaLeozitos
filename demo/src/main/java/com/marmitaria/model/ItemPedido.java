package com.marmitaria.model;

public class ItemPedido {
    private Marmita marmita;
    private int quantidade;
    private double precoUnitario; 

    public ItemPedido(Marmita marmita, int quantidade) {
        this.marmita = marmita;
        this.quantidade = quantidade;
        this.precoUnitario = marmita.getPreco();
    }

    public ItemPedido() {}

    public double getSubtotal() {
        return this.precoUnitario * this.quantidade;
    }

    public Marmita getMarmita() {
        return marmita;
    }

    public int getQuantidade() {
        return quantidade;
    }
}