package com.marmitaria.messaging;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {
    public static final String PEDIDOS_EXCHANGE = "marmitaria.pedidos.exchange";
    public static final String PEDIDOS_QUEUE = "marmitaria.pedidos.queue";
    public static final String PEDIDOS_ROUTING_KEY = "pedido.criado";

    @Bean
    public TopicExchange pedidosExchange() {
        return new TopicExchange(PEDIDOS_EXCHANGE);
    }

    @Bean
    public Queue pedidosQueue() {
        return new Queue(PEDIDOS_QUEUE, true);
    }

    @Bean
    public Binding pedidosBinding(Queue pedidosQueue, TopicExchange pedidosExchange) {
        return BindingBuilder.bind(pedidosQueue).to(pedidosExchange).with(PEDIDOS_ROUTING_KEY);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
