package wtt.utils;

import cn.hutool.json.JSONUtil;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.BooleanUtils;
import org.apache.commons.lang.StringUtils;
import org.redisson.api.RBloomFilter;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import wtt.config.RedissonConfig;
import wtt.pojo.Goods;

import java.time.LocalDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.BiFunction;
import java.util.function.Function;

@Slf4j
@Component
public class RedisClient {
private final RedisTemplate redisTemplate;
    public RedisClient(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    //开启线程池
    private static final ExecutorService CACHE_REBUILD_EXECUTOR= Executors.newFixedThreadPool(10);
  public void hashset(String key, String hashKey, Object value, Long time, TimeUnit unit){
    redisTemplate.opsForHash().put(key,hashKey, JSONUtil.toJsonStr(value));
    redisTemplate.expire(key,time,unit);
  }
//缓存击穿问题
  //设置过期时间
  public void setWithLogicalExpire(String key, String hashKey, Object value, Long time, TimeUnit unit){
    //设置逻辑过期
      RedisTime redisTime=new RedisTime();
      redisTime.setData(value);
      redisTime.setExpireTime(LocalDateTime.now().plusSeconds(time));
      //写入redis
      redisTemplate.opsForHash().put(key,hashKey,JSON.toJSONString(redisTime));
  }

  //缓存穿透
  public <R,ID,HK>List<R> queryWithPassThrough(
          String keyPrefix, ID id,HK hk, Class<R> type,
          Function<HK,List<R>> dbFallBack,Long time,TimeUnit unit
  ){
          String key=keyPrefix+id;
          String hashKey= (String) hk;


          //redis查询缓存
          String json= (String) redisTemplate.opsForHash().get(key,hashKey);
          if (StringUtils.isNotBlank(json)){
              //System.out.println("----------------------");
              return JSONUtil.toList(json,type);
          }
          //判读那命中的值是否为空值
          if (json!=null){
              return null;
          }
        List<R> list=dbFallBack.apply(hk);
          if(list==null){
              redisTemplate.opsForHash().put(key,hashKey,"");
              redisTemplate.expire(key,time,unit);
          }

          this.hashset(key,hashKey,JSONUtil.toJsonStr(list),time,unit);
          return list;
      }
      //布隆过滤
    public <R,ID,HK>List<R> queryWithPassThroughBloom(
            String keyPrefix, ID id,HK hk, Class<R> type,
            Function<HK,List<R>> dbFallBack,Long time,TimeUnit unit
    ){
        String key=keyPrefix+id;
        String hashKey= (String) hk;
        //redis查询缓存
        String json= (String) redisTemplate.opsForHash().get(key,hashKey);
        if (StringUtils.isNotBlank(json)){
            //System.out.println("----------------------");
            return JSONUtil.toList(json,type);
        }
        List<R> list=dbFallBack.apply(hk);
        this.hashset(key,hashKey,JSONUtil.toJsonStr(list),time,unit);
        return list;
    }
    //逻辑过期时间
    public <R,ID,HK> List<R> queryWithLogicalExpire(String keyPrefix,ID id,
                                                    HK hk,Class<R> type,
                                                    Function<HK,List<R>> dbFallBack, Long time, TimeUnit unit){
        String key=keyPrefix+id;
        String hashKey= (String) hk;
        //1redis查询商品缓存
        String json= (String) redisTemplate.opsForHash().get(key,hashKey);
        List<R> list;
        if (StringUtils.isBlank(json)){
            return null;
        }
        //System.out.println(json+"------------------------goodsjon");
        //4.redis命中，先把json反序列化为对象
        RedisTime redisTime= JSON.parseObject(json,RedisTime.class);
        // System.out.println(redisTime+"---------------------------reds");
        list= (List<R>) redisTime.getData();
        LocalDateTime expireTime=redisTime.getExpireTime();
        //5.判断是否过期
        if (expireTime.isAfter(LocalDateTime.now())){
            //6.未过期返回商品数据
            return list;
        }
        //7.已过期，要缓存重建
        //8.缓存重建
        //获取互斥锁
        boolean f=tryLock("lock:"+key);
        //System.out.println(f+"-----------------------f");
        //判断是否获取成功
        if (f){
            //成功开启线程，实现缓存重建
            CACHE_REBUILD_EXECUTOR.submit(()->{
                try {
                   List<R> r=dbFallBack.apply(hk);
                  //  System.out.println(r+"------------------------------r");
                    this.setWithLogicalExpire(key,hashKey,r,time,unit);
                }catch (Exception e){
                    throw new RuntimeException(e);
                }finally {
                    unlock("lock:"+key);
                }
            });
        }
        //9.返回过期数据
        return list;
    }

    //获取锁
    private boolean tryLock(String key){
        Boolean flag=redisTemplate.opsForValue().setIfAbsent(key,"1",10,TimeUnit.SECONDS);
        return BooleanUtils.isTrue(flag);
    }
    //删掉锁
    private void unlock(String key){
        redisTemplate.delete(key);
    }
 }
