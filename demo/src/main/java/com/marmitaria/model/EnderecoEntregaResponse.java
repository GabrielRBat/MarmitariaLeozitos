package com.marmitaria.model;

public class EnderecoEntregaResponse {
    private Endereco endereco;
    private double taxaEntrega;

    public EnderecoEntregaResponse(Endereco endereco, double taxaEntrega) {
        this.endereco = endereco;
        this.taxaEntrega = taxaEntrega;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public double getTaxaEntrega() {
        return taxaEntrega;
    }

    public void setTaxaEntrega(double taxaEntrega) {
        this.taxaEntrega = taxaEntrega;
    }
}
