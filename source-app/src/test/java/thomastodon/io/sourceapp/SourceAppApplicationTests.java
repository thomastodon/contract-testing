package thomastodon.io.sourceapp;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ExtendWith(SpringExtension.class)
class SourceAppApplicationTests {

    @Test
    @DisplayName("the app puts something on the queue")
    void contextLoads() throws IOException {

        CachingConnectionFactory cachingConnectionFactory = new CachingConnectionFactory("localhost", 5672);
        cachingConnectionFactory.setUsername("guest");
        cachingConnectionFactory.setPassword("guest");

        int messageCount = cachingConnectionFactory
            .createConnection()
            .createChannel(false)
            .basicGet("egg-queue", false)
            .getMessageCount();

        assertThat(messageCount).isGreaterThan(0);
    }
}
