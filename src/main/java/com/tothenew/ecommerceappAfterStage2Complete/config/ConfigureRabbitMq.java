package com.tothenew.ecommerceappAfterStage2Complete.config;

import com.tothenew.ecommerceappAfterStage2Complete.services.OrderService;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ConfigureRabbitMq {

    public static final String EXCHANGE_NAME = "OrderExchange";
    public static final String QUEUE_NAME = "OrderQueue";

    @Bean
    Queue createQueue() {
        return new Queue(QUEUE_NAME,false);
    }

    @Bean
    TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    @Bean
    Binding binding(Queue q, TopicExchange exchange) {
        return BindingBuilder.bind(q).to(exchange).with("Order.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory
            , MessageListenerAdapter messageListenerAdapter){
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(QUEUE_NAME);
        container.setMessageListener(messageListenerAdapter);
        return container;
    }

    @Bean
    MessageListenerAdapter listenerAdapter(OrderService order){
        return new MessageListenerAdapter(order, "createOrder");
    }
}
