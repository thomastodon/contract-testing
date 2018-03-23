package thomastodon.io.sourceapp;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

    @Bean
    AmqpTemplate amqpTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
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
}
