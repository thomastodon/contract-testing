package thomastodon.io.sourceapp;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.Exchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.amqp.dsl.AmqpOutboundEndpointSpec;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.MessageChannel;

@Configuration
public class OutboundFlowConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
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
    public MessageChannel amqpOutboundChannel() {
        return new DirectChannel();
    }

    @Bean
    Exchange eggExchange() {
        return new EggExchange();
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
        MessageChannel amqpOutboundChannel,
        AmqpOutboundEndpointSpec toEggCreated,
        GenericTransformer<Egg, String> eggPackager
    ) {
        return IntegrationFlows
            .from(amqpOutboundChannel)
            .transform(eggPackager)
            .handle(toEggCreated)
            .get();
    }
}
