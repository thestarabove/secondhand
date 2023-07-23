package wtt.utils;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
public class RedisTime {
 private LocalDateTime expireTime;
 private Object data;
}
