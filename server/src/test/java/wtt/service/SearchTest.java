package wtt.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class SearchTest {
    @Autowired
    private SearchRedisService searchRedisService;

    @Test
    public void incrementScoreByUserIdTest(){
//       int i=  searchRedisService.incrementScoreByUserId("wwwxxx");
//        System.out.println(i+"chenggong");
       int res=searchRedisService.addSearchByUserId(8,"w");
        System.out.println(res);
    }
}
