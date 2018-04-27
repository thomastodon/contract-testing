package thomastodon.io.sourceapp;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Exchange;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class SourceAppApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext context = new SpringApplicationBuilder(SourceAppApplication.class).run(args);

        Exchange eggExchange = context.getBean(EggExchange.class);
        AmqpAdmin amqpAdmin = context.getBean(AmqpAdmin.class);
        amqpAdmin.declareExchange(eggExchange);

        HenHouse henHouse = context.getBean(HenHouse.class);
        henHouse.sendEgg();
	}
}
