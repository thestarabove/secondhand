package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.Likes;
import wtt.service.LikesService;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

import java.util.List;

@RestController
@RequestMapping("/like")
public class LikeTest {
    @Autowired
    private LikesService likesService;
    @GetMapping("getAllLikesByUser")
    public ResponseJsonMessage getAllLikesByUser(){
        List<Likes> likesList=likesService.getAllLikesByUser(1);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),likesList);
    }
    @GetMapping("getLikeByGood")
    public ResponseJsonMessage getLikeByGood(){
       boolean flag=likesService.getLikeByGood(1,2);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),flag);
    }
    @GetMapping("findAll")
    public ResponseJsonMessage findAllLikes(){
        List<Likes> likesList=likesService.findAllByPage(1,2,2);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),likesList);
    }
}
