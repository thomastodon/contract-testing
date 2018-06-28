package thomastodon.io.sourceapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpOutboundEndpointSpec;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.PollerSpec;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.transformer.GenericTransformer;

import java.util.function.Supplier;

@Configuration
public class OutboundFlowConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    Supplier<Egg> henHouse() {
        return Egg::new;
    }

    @Bean
    PollerSpec poller() {
        return Pollers.fixedRate(1000).maxMessagesPerPoll(1);
    }

    @Bean
    GenericTransformer<Egg, String> eggPackager(ObjectMapper objectMapper) {
        return egg -> {
            try {
                String payload = objectMapper.writeValueAsString(egg);
                System.out.println(payload);
                return payload;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    AmqpTemplate amqpTemplate(CachingConnectionFactory connectionFactory) {
        return new RabbitTemplate(connectionFactory);
    }

    @Bean
    AmqpOutboundEndpointSpec toEggCreated(
        @Value("${rabbitmq.exchangeName}") String exchangeName,
        @Value("${rabbitmq.routingKey}") String routingKey,
        AmqpTemplate amqpTemplate
    ) {
        return Amqp.outboundAdapter(amqpTemplate).exchangeName(exchangeName).routingKey(routingKey);
    }

    @Bean
    IntegrationFlow outboundFlow(
        PollerSpec poller,
        Supplier<Egg> henHouse,
        AmqpOutboundEndpointSpec toEggCreated,
        GenericTransformer<Egg, String> eggPackager
    ) {
        return IntegrationFlows
            .from(henHouse, c -> c.poller(poller))
            .transform(eggPackager)
            .handle(toEggCreated)
            .get();
    }

}
