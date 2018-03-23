package thomastodon.io.sinkapp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.amqp.core.Queue;

@Configuration
public class AmqpConfiguration {

    @Bean
    AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.afterPropertiesSet();
        return rabbitAdmin;
    }

    @Bean
    CachingConnectionFactory connectionFactory(
        @Value("${rabbitmq.host}") String rabbitmqHost,
        @Value("${rabbitmq.port}") Integer rabbitmqPort,
        @Value("${rabbitmq.username}") String rabbitmqUsername,
        @Value("${rabbitmq.password}") String rabbitmqPassword
    ) {
        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory(rabbitmqHost, rabbitmqPort);
        cachingConnectionFactory.setUsername(rabbitmqUsername);
        cachingConnectionFactory.setPassword(rabbitmqPassword);
        return cachingConnectionFactory;
    }

    @Bean
    String queueName(@Value("$rabbitmq.queueName") String queueName) {
        return queueName;
    }

    @Bean
    Queue queue(
        AmqpAdmin amqpAdmin,
        String queueName
    ) {
        Queue queue = new Queue(queueName);
        queue.setAdminsThatShouldDeclare(amqpAdmin);
        return queue;
    }

    @Bean
    Binding binding(
        AmqpAdmin amqpAdmin,
        String queueName,
        @Value("${rabbitmq.exchangeName}") String exchangeName,
        @Value("${rabbitmq.routingKey}") String routingKey
    ) {
        Binding binding = new Binding(queueName, Binding.DestinationType.QUEUE, exchangeName, routingKey, null);
        binding.setAdminsThatShouldDeclare(amqpAdmin);
        return binding;
    }
}
