package wtt.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMqConfig {

    public static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
    public static final String QUEUE_INFORM_SMS = "queue_inform_sms";
    public static final String QUEUE_INFORM_DELAY_ORDER = "queue_inform_delay_order";
    public static final String EXCHANGE_TOPICS_INFORM="exchange_topics_inform";
    //public static final String EXCHANGE_TOPICS_DELAY_INFORM="exchange_delay_inform";
    public static final String ROUTINGKEY_EMAIL="inform.#.email.#";
    public static final String ROUTINGKEY_SMS="inform.#.sms.#";
    public static final String ROUTINGKEY_DELAY_ORDER="inform.#.delayOrder.#";
    //声明交换机
    @Bean(EXCHANGE_TOPICS_INFORM)
    public Exchange EXCHANGE_TOPICS_INFORM(){
        //durable(true) 持久化，mq重启之后交换机还在
        return ExchangeBuilder.topicExchange(EXCHANGE_TOPICS_INFORM).durable(true).build();
    }
//   @Bean(EXCHANGE_TOPICS_DELAY_INFORM)
//   public CustomExchange EXCHANGE_TOPICS_DELAY_INFORM(){
//       Map<String,Object> args=new HashMap<>();
//       args.put("x-delay-type","direct");
//       return new CustomExchange(EXCHANGE_TOPICS_DELAY_INFORM,"x-delay-message",true,false,args);
//   }

    //声明QUEUE_INFORM_EMAIL队列
    @Bean(QUEUE_INFORM_EMAIL)
    public Queue QUEUE_INFORM_EMAIL(){
        return new Queue(QUEUE_INFORM_EMAIL);
    }
    //声明QUEUE_INFORM_SMS队列
    @Bean(QUEUE_INFORM_SMS)
    public Queue QUEUE_INFORM_SMS(){
        return new Queue(QUEUE_INFORM_SMS);
    }

    //声明QUEUE_INFORM_DELAY_ORDER队列
//    @Bean(QUEUE_INFORM_DELAY_ORDER)
//    public Queue delayQueue() {
//        return new Queue(QUEUE_INFORM_DELAY_ORDER, true);
//    }
    //ROUTINGKEY_EMAIL队列绑定交换机，指定routingKey
    @Bean
    public Binding BINDING_QUEUE_INFORM_EMAIL(@Qualifier(QUEUE_INFORM_EMAIL) Queue queue,
                                              @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_EMAIL).noargs();
    }
    //ROUTINGKEY_ORDER队列绑定交换机，指定routingKey
    @Bean
    public Binding BINDING_ROUTINGKEY_SMS(@Qualifier(QUEUE_INFORM_SMS) Queue queue,
                                          @Qualifier(EXCHANGE_TOPICS_INFORM) Exchange exchange){
        return BindingBuilder.bind(queue).to(exchange).with(ROUTINGKEY_SMS).noargs();
    }
    //ROUTINGKEY_DELAY_ORDER队列绑定交换机，指定routingKey
//    @Bean
//    public Binding BINDING_ROUTINGKEY_DELAY_ORDER(){
//        return BindingBuilder.bind(delayQueue()).to(EXCHANGE_TOPICS_DELAY_INFORM()).with(ROUTINGKEY_DELAY_ORDER).noargs();
//    }
}