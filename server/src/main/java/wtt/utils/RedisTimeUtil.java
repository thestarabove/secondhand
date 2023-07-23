package wtt.utils;

import cn.dev33.satoken.stp.SaTokenInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class RedisTimeUtil {
    private static final String CHANNEL_NAME = "myChannel";
    @Autowired
    private RedisTemplate<String,String> redisTemplate;
    @Scheduled(fixedDelay = 10000)
    public void publishMessage(SaTokenInfo saTokenInfo,Integer userId){
        String message="hello redisTime!";
        System.out.println("Message published to Redis");
        redisTemplate.convertAndSend(CHANNEL_NAME,message);
    }
}
