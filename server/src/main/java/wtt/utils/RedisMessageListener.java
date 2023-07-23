package wtt.utils;


import org.springframework.stereotype.Component;

@Component
public class RedisMessageListener {
    public void handleMessage(String message){
        System.out.println("Received message"+message);
    }
}
