package thomastodon.io.messagingadapter;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmqpConfiguration {

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
    AmqpTemplate amqpTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    AmqpAdmin amqpAdmin(CachingConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.afterPropertiesSet();
        return rabbitAdmin;
    }
}
