package thomastodon.io.sourceapp;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
public class HenHouse {

    private MessageChannel amqpOutboundChannel;

    public HenHouse(MessageChannel amqpOutboundChannel) {
        this.amqpOutboundChannel = amqpOutboundChannel;
    }

    public void sendEgg() {
        Message<Egg> message = MessageBuilder.withPayload(new Egg()).build();
        amqpOutboundChannel.send(message);
    }
}
