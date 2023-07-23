package wtt.service;

import org.springframework.stereotype.Service;
import wtt.pojo.Goods;

@Service
public interface CartGoodService {
    Goods getGood(Integer goodId);

}
