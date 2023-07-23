package wtt.service.serviceImpl;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.core.Cursor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import wtt.service.SearchRedisService;
import wtt.utils.ComFoundException;
import wtt.utils.RedisName;
import wtt.utils.RedisUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Transactional
@Service("SearchRedisService")
public class SearchRedisServiceImpl implements SearchRedisService {
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public int addSearchByUserId(Integer userId, String searchKey) {
        String Id= String.valueOf(userId);
        System.out.println(Id);
        try {
            redisUtil.hset("searchHistory:"+Id,searchKey,null);
        }catch (ComFoundException c){
            throw new ComFoundException("查询商品缓存失败！");
        }
        return 1;
    }

    //新增一条用户在搜索栏的历史记录
    @Override
    public int addSearchHistoryByUserId(Integer userId, String searchKey) {
        String Id= String.valueOf(userId);
        //System.out.println(Id+"id");
        String history=  redisUtil.hget("searchHistory:"+Id,searchKey);

      //  System.out.println(history+"...key");
        boolean b= Boolean.parseBoolean(history);
       // System.out.println(b+"panduan");
        if (b){
            boolean hk =redisUtil.hHasKey("searchHistory:"+Id,searchKey);
           // System.out.println(hk);
            if (hk){
                return 0;
            }else {
                redisUtil.hset("searchHistory:"+Id,searchKey,"1");
            }
        }else {
            redisUtil.hset("searchHistory:"+Id,searchKey,"1");
        }
        return 0;
    }
    //删除个人历史记录
    @Override
    public int deleteSearchHistoryByUserId(Integer userId, String searchKey) {
        String Id= String.valueOf(userId);
        String history=  redisUtil.hget("searchHistory:"+Id,searchKey);
        try {
            redisUtil.hdel("searchHistory:"+Id,searchKey);
        }catch (ComFoundException c){
            throw new ComFoundException("删除个人历史记录失败！");
        }
        return 1;
    }

    //获取个人历史记录数据
    @Override
    public List<String> getSearchHistoryByUserId(Integer userId) {
        List<String> stringList =new ArrayList<>();
        String Id= String.valueOf(userId);
       // String history= String.valueOf(redisTemplate.opsForHash().get(Id,null));
        boolean b=redisUtil.hasKey("searchHistory:"+Id);
        if (b){
            Cursor<Map.Entry<Object,Object>> cursor=redisTemplate.opsForHash().scan("searchHistory:"+Id, ScanOptions.NONE);

            while (cursor.hasNext()){
                Map.Entry<Object,Object> map=cursor.next();
                String key=map.getKey().toString();
               // System.out.println(key);
                stringList.add(key);
               // System.out.println(stringList);
            }
           return stringList;
        }
        return null;
    }
    //新增一条热词搜索记录，将用户输入的热词存储下来
    @Override
    public int incrementScoreByUserId(int userId,String searchKey) {
        Long now=System.currentTimeMillis();
        ZSetOperations zSetOperations=redisTemplate.opsForZSet();
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        List<String> title=new ArrayList<>();
        title.add(searchKey);
        title.stream()
                .forEach(tle->{
                    try {
                        if (zSetOperations.score(RedisName.GOOD_REDIS_NAME+"title",tle)<=0){
                            zSetOperations.add(RedisName.GOOD_REDIS_NAME+"title",tle,0);
                            valueOperations.set(RedisName.TIME_SEARCH_REDIS_NAME +tle,String.valueOf(now));
                        }
                    }catch (Exception e){
                        zSetOperations.add(RedisName.GOOD_REDIS_NAME+"title",tle,0);
                        valueOperations.set(RedisName.TIME_SEARCH_REDIS_NAME+tle,String.valueOf(now));
                    }
                })
                ;
        this.incrementScore(searchKey);
        return 1;
    }
    //根据searchkey搜索其相关最热的前十名 (如果searchkey为null空，则返回redis存储的前十最热词条)
    @Override
    public List<String> getHotList(String searchKey) {
        String key=searchKey;
        Long now=System.currentTimeMillis();
        List<String> result=new ArrayList<>();
        ZSetOperations zSetOperations=redisTemplate.opsForZSet();
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        Set<String> value=zSetOperations.reverseRangeByScore(RedisName.GOOD_REDIS_NAME+"title",0,Double.MAX_VALUE);
        //key不为空的时候 推荐相关的最热前十名
        if(org.apache.commons.lang.StringUtils.isNotEmpty(searchKey)){
            value.stream()
                    .filter(val -> StringUtils.containsIgnoreCase(val, key))
                    .filter(val -> {
                        if (result.size() > 9) {
                            return false;
                        }
                        Long time = Long.valueOf(valueOperations.get(val));
                        if ((now - time) < 2592000000L) {//返回最近一个月的数据
                            return true;
                        } else {//时间超过一个月没搜索就把这个词热度归0
                            zSetOperations.remove(RedisName.GOOD_REDIS_NAME+"title", val, 0);
                            return false;
                        }
                    })
                    .forEach(val -> result.add(val));
        }else {
            value.stream()
                    .filter(val -> {
                        if (result.size()>9){
                            return false;
                        }   Long time =Long.valueOf(valueOperations.get(val));
                        if((now -time)< 2592000000L){
                            return true;
                        }else {
                            zSetOperations.add(RedisName.GOOD_REDIS_NAME+"title",val,0);
                            return false;
                        }

                    })
                    .forEach(val->result.add(val));
        }
        return result;
    }
    //每次点击给相关词searchkey热度 +1
    @Override
    public int incrementScore(String searchKey) {
        String key=searchKey;
        Long now=System.currentTimeMillis();
        ZSetOperations zSetOperations=redisTemplate.opsForZSet();
        ValueOperations<String,String> valueOperations=redisTemplate.opsForValue();
        zSetOperations.incrementScore(RedisName.GOOD_REDIS_NAME+"title",key,1);
        valueOperations.getAndSet(RedisName.TIME_SEARCH_REDIS_NAME+key,String.valueOf(now));
        return 1;
    }
    // 判断商品是否存在于购物车中
    @Override
    public Boolean isProductInSearch(String userId, String goodName) {
        return redisTemplate.opsForHash().hasKey("search:"+userId,goodName);
    }

    @Override
    public boolean handleProductInSearch(String name) {
        Set<String> userId=redisTemplate.keys("search:"+"*");
        for (String u:userId){
            String searchUserId=u.replace("search:","");
            if (isProductInSearch(searchUserId,name)){
                redisTemplate.opsForHash().delete(searchUserId,name);
            }
        }
        return true;
    }
}
