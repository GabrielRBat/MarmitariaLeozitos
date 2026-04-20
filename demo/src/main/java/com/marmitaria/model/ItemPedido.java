package com.marmitaria.model;

public class ItemPedido {
    private Marmita marmita;
    private int quantidade;
    private double precoUnitario; 

    public ItemPedido(Marmita marmita, int quantidade, double precoUnitario) {
        this.marmita = marmita;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

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

    public void setMarmita(Marmita marmita) {
        this.marmita = marmita;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }
}