package com.marmitaria.client;

import com.marmitaria.model.Endereco;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@Component
public class ViaCepClient {
    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json/";

    private final RestTemplate restTemplate;

    public ViaCepClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public Endereco buscarEndereco(String cep) {
        String cepSomenteNumeros = normalizarCep(cep);

        try {
            Endereco endereco = restTemplate.getForObject(VIA_CEP_URL, Endereco.class, cepSomenteNumeros);
            if (endereco == null || endereco.isErro()) {
                throw new RuntimeException("CEP nao encontrado na ViaCEP.");
            }
            return endereco;
        } catch (RestClientException ex) {
            throw new RuntimeException("Nao foi possivel consultar o CEP na ViaCEP.", ex);
        }
    }

    private String normalizarCep(String cep) {
        if (cep == null) {
            throw new RuntimeException("O CEP de entrega e obrigatorio.");
        }

        String cepSomenteNumeros = cep.replaceAll("\\D", "");
        if (cepSomenteNumeros.length() != 8) {
            throw new RuntimeException("O CEP deve conter 8 digitos.");
        }

        return cepSomenteNumeros;
    }
}
