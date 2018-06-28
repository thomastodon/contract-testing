package thomastodon.io.sinkapp;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.amqp.dsl.Amqp;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.transformer.GenericTransformer;
import org.springframework.messaging.SubscribableChannel;

import java.io.IOException;

@Configuration
public class InboundFlowConfiguration {

    @Bean
    ObjectMapper objectMapper() {
        return new ObjectMapper();
    }

    @Bean
    GenericTransformer<Egg, Egg> paintTheEgg() {
        return egg -> {
            egg.setColor("purple");
            System.out.println(egg);
            return egg;
        };
    }

    @Bean
    GenericTransformer<String, Egg> toEgg(ObjectMapper objectMapper) {
        return payload -> {
            try {
                return objectMapper.readValue(payload, Egg.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        };
    }

    @Bean
    IntegrationFlow inboundFlow(
        @Value("${rabbitmq.queueName}") String queueName,
        CachingConnectionFactory connectionFactory,
        SubscribableChannel paintedEggs,
        GenericTransformer<Egg, Egg> paintTheEgg,
        GenericTransformer<String, Egg> toEgg
    ) {
        return IntegrationFlows
            .from(Amqp.inboundAdapter(connectionFactory, queueName))
            .transform(toEgg)
            .transform(paintTheEgg)
            .channel(paintedEggs)
            .get();
    }

    @Bean
    SubscribableChannel paintedEggs() {
        return new PublishSubscribeChannel();
    }
}
