package exercise;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;


@Component
public class MessageSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(MessageSender.class);

    // Класс, который даёт простой доступ к брокеру сообщений RabbitMQ
    // Позволяет отправлять и получать сообщения
    @Autowired
    RabbitTemplate rabbitTemplate;

    // BEGIN
    public void sendMessage(String message) {
        // Отправляем сообщение в обменник с ключом "key"
        // В зависимости от ключа, сообщение будет отправлено в нужную очередь
        LOGGER.info("Sending message to the queue...");

        rabbitTemplate.convertAndSend("exchange", "key", message);

        LOGGER.info("Message sent successfully to the queue!!!");
    }
    // END
}
