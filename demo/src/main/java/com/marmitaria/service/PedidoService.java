package com.marmitaria.service;

import com.marmitaria.exception.EntidadeNaoEncontradaException;
import com.marmitaria.messaging.PedidoProducer;
import com.marmitaria.model.EnderecoEntregaResponse;
import com.marmitaria.model.ItemPedido;
import com.marmitaria.model.Marmita;
import com.marmitaria.model.Pedido;
import com.marmitaria.model.PedidoRequest;
import com.marmitaria.repository.MarmitaRepository;
import com.marmitaria.repository.PedidoRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final MarmitaRepository marmitaRepository;
    private final EnderecoService enderecoService;
    private final PedidoProducer pedidoProducer;

    public PedidoService(PedidoRepository pedidoRepository, MarmitaRepository marmitaRepository, EnderecoService enderecoService, PedidoProducer pedidoProducer) {
        this.pedidoRepository = pedidoRepository;
        this.marmitaRepository = marmitaRepository;
        this.enderecoService = enderecoService;
        this.pedidoProducer = pedidoProducer;
    }

    public Pedido criarPedido(List<ItemPedido> itensSimples) {
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(itensSimples);
        Pedido novoPedido = new Pedido(itensCompletos);
        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        pedidoProducer.publicarPedidoCriado(pedidoSalvo);
        return pedidoSalvo;
    }

    public Pedido criarPedidoComEntrega(PedidoRequest request) {
        validarRequestPedido(request);
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(request.getItens());
        Pedido novoPedido = new Pedido(itensCompletos);
        aplicarDadosEntrega(novoPedido, request.getCepEntrega());

        Pedido pedidoSalvo = pedidoRepository.save(novoPedido);
        pedidoProducer.publicarPedidoCriado(pedidoSalvo);
        return pedidoSalvo;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Pedido buscarPorId(Long id) {
        Pedido pedido = pedidoRepository.findById(id);
        if (pedido == null) {
            throw new EntidadeNaoEncontradaException("Pedido com ID " + id + " nao encontrado.");
        }
        return pedido;
    }

    public Pedido atualizar(Long id, List<ItemPedido> novosItensSimples) {
        Pedido existente = buscarPorId(id);
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(novosItensSimples);

        existente.setItens(itensCompletos);
        existente.recalcularValorTotal();

        pedidoRepository.update(existente);
        return existente;
    }

    public Pedido atualizarComEntrega(Long id, PedidoRequest request) {
        validarRequestPedido(request);
        Pedido existente = buscarPorId(id);
        List<ItemPedido> itensCompletos = validarEPuxarMarmitas(request.getItens());

        existente.setItens(itensCompletos);
        aplicarDadosEntrega(existente, request.getCepEntrega());

        pedidoRepository.update(existente);
        return existente;
    }

    public void deletar(Long id) {
        buscarPorId(id);
        pedidoRepository.delete(id);
    }

    private List<ItemPedido> validarEPuxarMarmitas(List<ItemPedido> itens) {
        if (itens == null || itens.isEmpty()) {
            throw new RuntimeException("O pedido deve ter pelo menos uma marmita.");
        }

        return itens.stream().map(item -> {
            if (item.getMarmita() == null || item.getMarmita().getId() == null) {
                throw new RuntimeException("Informe o ID da marmita em todos os itens.");
            }
            if (item.getQuantidade() <= 0) {
                throw new RuntimeException("A quantidade de cada item deve ser maior que zero.");
            }

            Marmita marmita = marmitaRepository.findById(item.getMarmita().getId());
            if (marmita == null) {
                throw new EntidadeNaoEncontradaException("Marmita com ID " + item.getMarmita().getId() + " nao encontrada.");
            }
            return new ItemPedido(marmita, item.getQuantidade());
        }).collect(Collectors.toList());
    }

    private void validarRequestPedido(PedidoRequest request) {
        if (request == null) {
            throw new RuntimeException("Dados do pedido sao obrigatorios.");
        }
    }

    private void aplicarDadosEntrega(Pedido pedido, String cepEntrega) {
        EnderecoEntregaResponse entrega = enderecoService.simularEntrega(cepEntrega);
        pedido.setCepEntrega(entrega.getEndereco().getCep());
        pedido.setEnderecoEntrega(entrega.getEndereco().formatar());
        pedido.setTaxaEntrega(entrega.getTaxaEntrega());
        pedido.recalcularValorTotal();
    }
}
