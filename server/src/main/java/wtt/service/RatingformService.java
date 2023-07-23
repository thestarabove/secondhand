package wtt.service;

import wtt.pojo.Likes;
import wtt.pojo.Ratingform;

import java.util.List;

public interface RatingformService {
    public List<Ratingform> getAllRatingFormByUser(Integer userId);
    public List<Ratingform> getRatingFormByEvaluated(Integer userId);
    public boolean addRatingFormByUserId(Integer userId,Integer orderId);
    public int deleteRatingFormByEvaluateId(Integer evaluateId);
    public int updateRatingFormByEvaluateId(Ratingform ratingform);
    List<Ratingform> findFormByAdmin();
    int  updateRatingFormByAdmin(Integer ratingId);
    int  failRatingFormByAdmin(Integer ratingId);

}
