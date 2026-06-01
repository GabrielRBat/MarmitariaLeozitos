package com.marmitaria.messaging;

import com.marmitaria.model.Pedido;
import com.marmitaria.model.PedidoMensagem;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PedidoProducer {
    private final RabbitTemplate rabbitTemplate;

    public PedidoProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void publicarPedidoCriado(Pedido pedido) {
        try {
            rabbitTemplate.convertAndSend(
                RabbitMqConfig.PEDIDOS_EXCHANGE,
                RabbitMqConfig.PEDIDOS_ROUTING_KEY,
                new PedidoMensagem(pedido)
            );
        } catch (AmqpException ex) {
            System.out.println("Nao foi possivel publicar o pedido no RabbitMQ: " + ex.getMessage());
        }
    }
}
