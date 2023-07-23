package wtt.controllerTest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import wtt.pojo.Ratingform;
import wtt.service.RatingformService;
import wtt.utils.ResponseJsonStatus;
import wtt.vo.ResponseJsonMessage;

import java.util.List;

@RestController
@RequestMapping("/ratingForm")
public class RatingFormTest {

    @Autowired
    private RatingformService ratingformService;


    @GetMapping("/getAll")
    public ResponseJsonMessage getAllRatingForms(){
        List<Ratingform> ratingformList=ratingformService.getAllRatingFormByUser(1);

        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),ratingformList);
    }

    @GetMapping("/update")
    public ResponseJsonMessage updateRatingForm(){
        Ratingform ratingform=new Ratingform();
        ratingform.setEvaluateId(2);
        ratingform.setStarRating("5");
        ratingform.setContent("测试内容");
        int flag=ratingformService.updateRatingFormByEvaluateId(ratingform);
        return new ResponseJsonMessage(ResponseJsonStatus.SUCCESS.getStatus(), ResponseJsonStatus.SUCCESS.getMessage(),flag);
    }
}
