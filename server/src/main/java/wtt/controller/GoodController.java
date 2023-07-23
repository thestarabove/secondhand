package wtt.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;
import wtt.pojo.Goods;
import wtt.pojo.User;
import wtt.service.GoodsService;
import wtt.service.SearchRedisService;
import wtt.utils.*;
import wtt.vo.GoodVo;
import wtt.vo.ResponseJsonMessage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

//下架商品，修改商品，查询商品，显示商品主界面
@RestController
@RequestMapping("/secondhand/good")
public class GoodController {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private SearchRedisService searchRedisService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private RedisTemplate redisTemplate;

    @GetMapping("/findGood")
    public ResponseJsonMessage findGood(String state){
        System.out.println(state);
        Integer startIndex=(Integer.valueOf(state)-1)*4;
       List<Goods> goods=goodsService.findGoodsByPage(Integer.valueOf(startIndex));
        if (goods==null){
            throw new ComFoundException("获取商品失败");
        }
        //System.out.println(goods+"---------------goods");
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goods);
    }
    @GetMapping("/findAllGood")
    public ResponseJsonMessage findAllGood(){
        List<Goods> goods=goodsService.findAllGoods();
        if (goods==null){
            throw new ComFoundException("获取商品失败");
        }

        //System.out.println(goods+"-------------------goods");
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goods.size());
    }
    @GetMapping("/searchGood")
    public ResponseJsonMessage searchGood(@RequestParam("goodName")String goodName){
        SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
       Integer userId=8;
       String msg="查询商品不存在！";
        List<Goods> goodsList;
        OptionalNull optionalNull=new OptionalNull();
        Optional<Integer> nullClass = optionalNull.isNullClass(userId);
         if (nullClass.isEmpty()){
             return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),"用户id为空");
         }
        goodsList=goodsService.findGoodByNamePage(userId,goodName);
        if (goodsList==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),msg);
        }
//        加入历史记录
        searchRedisService.addSearchHistoryByUserId(userId,goodName);
            //加入热点记录
            //记录加1
            searchRedisService.incrementScoreByUserId(userId,goodName);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goodsList);
    }
   @GetMapping("/searchHistory")
    public ResponseJsonMessage searchHistory(){
       SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
      Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());
      List<String> searchList=searchRedisService.getSearchHistoryByUserId(userId);
      if (searchList==null){
          throw new ComFoundException("获取查询历史记录失败！");
      }
      Collections.reverse(searchList);
       System.out.println(".........."+searchList);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),searchList);
   }
   @GetMapping("/deleteHistory")
    public ResponseJsonMessage deleteHistory(@RequestParam("goodName")String goodName){
       SaTokenInfo saTokenInfo = StpUtil.getTokenInfo();
       Integer userId= (Integer) redisUtil.get("token:"+saTokenInfo.getTokenValue());

       int f=searchRedisService.deleteSearchHistoryByUserId(userId,goodName);
       if (f!=1){
           throw new ComFoundException("删除查询记录失败！");
       }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
   }
   @GetMapping("/getHotSearch")
    public ResponseJsonMessage getHotSearch(){
     List<String>s= searchRedisService.getHotList(null);
     if (s==null){
         throw new ComFoundException("查询热词失败！");
     }
      // System.out.println(s);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),s);
   }
}


