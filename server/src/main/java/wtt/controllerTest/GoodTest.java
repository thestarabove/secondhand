package wtt.controllerTest;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.Goods;
import wtt.service.GoodsService;
import wtt.utils.RedisClient;
import wtt.utils.RedisName;
import wtt.utils.ResponseJsonStatus;

import wtt.vo.ResponseJsonMessage;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/good")
public class GoodTest {
    @Autowired
    private GoodsService goodsService;
    @Autowired
    private RedisClient redisClient;

    @GetMapping("/searchGoods")
    public ResponseJsonMessage searchGood(){
        Integer userId= 8;
        String msg="查询商品不存在！";
        String goodName="xilin";
        //  Integer id= 1;
        List<Goods> goodsList;
        goodsList=goodsService.findGoodsLock(userId,goodName);

        redisClient.setWithLogicalExpire(RedisName.GOOD_REDIS_NAME+userId,goodName,goodsList,10L, TimeUnit.SECONDS);
        if (goodsList==null){
            return new ResponseJsonMessage(ResponseJsonStatus.FAILURE.getStatus(), ResponseJsonStatus.FAILURE.getMessage(),msg);
        }
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),goodsList);
    }

}
