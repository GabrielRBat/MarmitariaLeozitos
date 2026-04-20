package com.marmitaria.controller;

import com.marmitaria.model.Marmita;
import com.marmitaria.service.MarmitaService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/marmitas") 
public class MarmitaController {

    private final MarmitaService service;

    public MarmitaController(MarmitaService service) {
        this.service = service;
    }

    @GetMapping
    public List<Marmita> listarTodos() {
        return service.listarTodas();
    }

    @PostMapping
    public Marmita criar(@RequestBody Marmita marmita) {
        return service.salvar(marmita);
    }

    @PutMapping("/{id}")
    public Marmita atualizar(@PathVariable Long id, @RequestBody Marmita marmita) {
        return service.atualizar(id, marmita);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Long id) {
        service.deletar(id);
    }

    @GetMapping("/{id}")
    public Marmita buscarPorId(@PathVariable Long id) {
        return service.buscarPorId(id);
    }
}