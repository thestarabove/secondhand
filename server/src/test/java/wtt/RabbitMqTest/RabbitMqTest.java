package wtt.RabbitMqTest;

import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.config.DelayRabbitMqConfig;
import wtt.config.RabbitMqConfig;
import wtt.utils.RabbitMqUtil;

@SpringBootTest
public class RabbitMqTest {

    @Autowired
     RabbitTemplate rabbitTemplate;
    @Autowired
    RabbitMqUtil rabbitMqUtil;

    @Test
    public void RabbitMqTest(){
         String message="send email message to user";
         rabbitTemplate.convertAndSend(DelayRabbitMqConfig.DELAYED_EXCHANGE_NAME,DelayRabbitMqConfig.DELAYED_ROUTING_KEY,message,
                 message1 -> {
               message1.getMessageProperties().setDelay(10);
               return message1;
                 });

    }


    public void RabbitMqReceive(){

        System.out.println();
    }

}
