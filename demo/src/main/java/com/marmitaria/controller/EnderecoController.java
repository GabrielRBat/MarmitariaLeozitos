package com.marmitaria.controller;

import com.marmitaria.model.Endereco;
import com.marmitaria.model.EnderecoEntregaResponse;
import com.marmitaria.service.EnderecoService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/enderecos")
public class EnderecoController {
    private final EnderecoService enderecoService;

    public EnderecoController(EnderecoService enderecoService) {
        this.enderecoService = enderecoService;
    }

    @GetMapping("/{cep}")
    public Endereco buscarPorCep(@PathVariable String cep) {
        return enderecoService.buscarEndereco(cep);
    }

    @GetMapping("/{cep}/entrega")
    public EnderecoEntregaResponse simularEntrega(@PathVariable String cep) {
        return enderecoService.simularEntrega(cep);
    }
}
