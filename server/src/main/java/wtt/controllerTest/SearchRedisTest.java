package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.service.SearchRedisService;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

@RestController
@RequestMapping("/search")
public class SearchRedisTest {
    @Autowired
    private SearchRedisService searchRedisService;

    @GetMapping("/add")
    public ResponseJsonMessage addSearch(){
       Integer userId=1;
       String searchkey="xilin";
        searchRedisService.addSearchByUserId(userId,searchkey);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @GetMapping("/addHistory")
    public ResponseJsonMessage addSearchHistory(){
        Integer userId=1;
        String searchkey="xilin";
        searchRedisService.addSearchHistoryByUserId(userId,searchkey);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
    @GetMapping("/deleteHistory")
    public ResponseJsonMessage deleteSearchHistory(){
        Integer userId=1;
        String searchkey="xilin";
        searchRedisService.deleteSearchHistoryByUserId(userId,searchkey);
        return  new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage());
    }
}
