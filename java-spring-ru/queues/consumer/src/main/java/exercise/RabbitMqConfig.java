package exercise;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMqConfig {

    // BEGIN
    @Bean
    Queue queue() {
        return new Queue("queue", false);
    }
    // END
}
