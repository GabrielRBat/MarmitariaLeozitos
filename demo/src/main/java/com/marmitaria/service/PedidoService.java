package com.marmitaria.service;

import com.marmitaria.exception.EntidadeNaoEncontradaException;
import com.marmitaria.model.ItemPedido;
import com.marmitaria.model.Marmita;
import com.marmitaria.model.Pedido;
import com.marmitaria.repository.MarmitaRepository;
import com.marmitaria.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MarmitaRepository marmitaRepository;

    public PedidoService(PedidoRepository pedidoRepository, MarmitaRepository marmitaRepository) {
        this.pedidoRepository = pedidoRepository;
        this.marmitaRepository = marmitaRepository;
    }

    public Pedido criarPedido(List<ItemPedido> itensSimples) {
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(itensSimples);
        Pedido novoPedido = new Pedido(itensCompletos);
        return pedidoRepository.save(novoPedido);
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + id + " não encontrado.");
        }
        return pedido;
    }

    public Pedido atualizar(Long id, List<ItemPedido> novosItensSimples) {
        Pedido existente = buscarPorId(id);
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(novosItensSimples);

        existente.setItens(itensCompletos);
        // Recalcula o valor total baseado nos novos itens
        double novoTotal = itensCompletos.stream().mapToDouble(ItemPedido::getSubtotal).sum();
        existente.setValorTotal(novoTotal);

        pedidoRepository.update(existente);
        return existente;
    }

    public void deletar(Long id) {
        buscarPorId(id);
        pedidoRepository.delete(id);
    }

    private List<ItemPedido> validarEPuxarMarmitas(List<ItemPedido> itens) {
        return itens.stream().map(item -> {
            Marmita marmita = marmitaRepository.findById(item.getMarmita().getId());
            if (marmita == null) {
                throw new EntidadeNaoEncontradaException("Marmita com ID " + item.getMarmita().getId() + " não encontrada.");
            }
            return new ItemPedido(marmita, item.getQuantidade());
        }).collect(Collectors.toList());
    }
}
