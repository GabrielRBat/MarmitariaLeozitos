package com.marmitaria.messaging;

import com.marmitaria.model.PedidoMensagem;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class PedidoConsumer {
    @RabbitListener(queues = RabbitMqConfig.PEDIDOS_QUEUE)
    public void consumirPedidoCriado(PedidoMensagem mensagem) {
        System.out.println("Pedido recebido pelo RabbitMQ: ID " + mensagem.getPedidoId()
            + ", status " + mensagem.getStatus()
            + ", total " + mensagem.getValorTotal());
    }
}
