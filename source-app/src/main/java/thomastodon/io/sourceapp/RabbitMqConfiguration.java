package thomastodon.io.sourceapp;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfiguration {

    @Bean
    AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.afterPropertiesSet();
        return rabbitAdmin;
    }

    @Bean
    Exchange exchange(
        @Value("${rabbitmq.exchangeName}") String exchangeName
    ) {
        return new FanoutExchange(exchangeName, true, true);
    }

    @Bean
    Queue queue(
        AmqpAdmin amqpAdmin,
        @Value("${rabbitmq.queueName}") String queueName
    ) {
        Queue queue = new Queue(queueName);
        queue.setAdminsThatShouldDeclare(amqpAdmin);
        return queue;
    }

    @Bean
    Binding binding(
        AmqpAdmin amqpAdmin,
        @Value("${rabbitmq.queueName}") String queueName,
        @Value("${rabbitmq.exchangeName}") String exchangeName,
        @Value("${rabbitmq.routingKey}") String routingKey
    ) {
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);
        binding.setAdminsThatShouldDeclare(amqpAdmin);
        return binding;
    }
}
