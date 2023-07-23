package wtt.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import wtt.pojo.Goods;
import wtt.vo.GoodVo;

import java.util.List;

public interface GoodsService {
    Goods findGood(Integer second_hand_id);
    List<Goods> findAllGoods();
    List<Goods> findAllGoodsByUser(Integer userId);
    int addGood(Goods goods);
    int updateGood(Goods goods);
    int deleteGood(Integer id);
    List<Goods> findGoodsByPage(Integer state);
    List<Goods> findGoodByNamePage(Integer userId, String name);
    List<Goods> findAllGoodsBySeller(Integer userId,Integer state);
    List<Goods> findGoodsLock(Integer userId,String name);
}
