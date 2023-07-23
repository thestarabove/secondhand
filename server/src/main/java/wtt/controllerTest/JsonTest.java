package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.User;
import wtt.service.UserService;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/jsonFormat")
public class JsonTest {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public ResponseJsonMessage getUser(){
       User user =userService.getUser(1);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),user);
    }
    @GetMapping("/list")
    public ResponseJsonMessage getUserList(){
        List<wtt.Daomian.User> list=new ArrayList<>();
        wtt.Daomian.User user1=new wtt.Daomian.User("xilin",12);
        wtt.Daomian.User user2=new wtt.Daomian.User("xilin",13);
        list.add(user1);
        list.add(user2);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),list);
    }
   @GetMapping("/map")
    public ResponseJsonMessage getMap(){
        Map<String,Object> map=new HashMap<>();
       wtt.Daomian.User user=new wtt.Daomian.User("xilin",18);
       map.put("用户信息",user);
       map.put("性别","女");
       map.put("备注","可爱捏");
       map.put("手机号",11111);
       return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),map);
   }

}
