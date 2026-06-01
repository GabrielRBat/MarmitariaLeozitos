package com.marmitaria.service;

import com.marmitaria.client.ViaCepClient;
import com.marmitaria.model.Endereco;
import com.marmitaria.model.EnderecoEntregaResponse;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class EnderecoService {
    private static final Set<String> ESTADOS_SUDESTE = Set.of("SP", "RJ", "MG", "ES");

    private final ViaCepClient viaCepClient;

    public EnderecoService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public Endereco buscarEndereco(String cep) {
        return viaCepClient.buscarEndereco(cep);
    }

    public EnderecoEntregaResponse simularEntrega(String cep) {
        Endereco endereco = buscarEndereco(cep);
        return new EnderecoEntregaResponse(endereco, calcularTaxaEntrega(endereco));
    }

    public double calcularTaxaEntrega(Endereco endereco) {
        if ("SP".equalsIgnoreCase(endereco.getUf())) {
            return 5.0;
        }
        if (ESTADOS_SUDESTE.contains(endereco.getUf())) {
            return 10.0;
        }
        return 15.0;
    }
}
