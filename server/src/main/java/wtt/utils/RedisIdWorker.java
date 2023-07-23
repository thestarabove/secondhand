package wtt.utils;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

@Component
public class RedisIdWorker {
    /**
     * 当前的时间戳
     */
    private  static final  long BEGAN_TIMESTAMP=1689897600L;
    private  static final  long COUNT_BITS=32;
    private RedisTemplate redisTemplate;

    public RedisIdWorker(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public long nextId(String keyPrefix){
        //生成时间戳
        LocalDateTime now=LocalDateTime.now();
        long nowTimeSecond=now.toEpochSecond(ZoneOffset.UTC);
        long timestamp=nowTimeSecond-BEGAN_TIMESTAMP;
        //生成序列号
        String mMdd = now.format(DateTimeFormatter.ofPattern("yyyy:MM:dd"));
        Long increment = redisTemplate.opsForValue().increment("icr:" + keyPrefix + ":" + mMdd);
        Long RedisLongId=timestamp<<COUNT_BITS | increment;
        return RedisLongId;
    }
}
