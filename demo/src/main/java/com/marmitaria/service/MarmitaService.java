package com.marmitaria.service;

import com.marmitaria.exception.EntidadeNaoEncontradaException;
import com.marmitaria.model.Marmita;
import com.marmitaria.repository.MarmitaRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MarmitaService {

    private final MarmitaRepository repository;

    public MarmitaService(MarmitaRepository repository) {
        this.repository = repository;
    }

    public Marmita salvar(Marmita marmita) {
        validarMarmita(marmita);
        return repository.save(marmita);
    }

    public List<Marmita> listarTodas() {
        return repository.findAll();
    }

    public Marmita buscarPorId(Long id) {
        Marmita marmita = repository.findById(id);
        if (marmita == null) {
            throw new EntidadeNaoEncontradaException("Marmita com ID " + id + " não encontrada.");
        }
        return marmita;
    }

    public Marmita atualizar(Long id, Marmita marmitaAtualizada) {
        Marmita existente = buscarPorId(id);
        validarMarmita(marmitaAtualizada);
        
        existente.setNome(marmitaAtualizada.getNome());
        existente.setPreco(marmitaAtualizada.getPreco());
        existente.setCategoria(marmitaAtualizada.getCategoria());
        
        repository.update(existente);
        return existente;
    }

    public void deletar(Long id) {
        buscarPorId(id); // Garante que existe antes de deletar
        repository.delete(id);
    }

    private void validarMarmita(Marmita marmita) {
        if (marmita.getNome() == null || marmita.getNome().isBlank()) {
            throw new RuntimeException("O nome da marmita é obrigatório.");
        }
        if (marmita.getPreco() <= 0) {
            throw new RuntimeException("O preço da marmita deve ser maior que zero.");
        }
    }
}
