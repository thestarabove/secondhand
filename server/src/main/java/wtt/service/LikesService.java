package wtt.service;

import wtt.pojo.Likes;

import java.util.List;

public interface LikesService {
    public List<Likes> getAllLikesByUser(Integer userId);
    public List<Likes> findAllByPage(Integer userId,Integer pageNum,Integer pageSize);
    public boolean getLikeByGood(Integer goodId, Integer userId);
    public Likes getLikeByLikeId(Integer likeId);
    public int addLikeByGoodId(Likes likes);
    public int deleteLikesByLikesId(Integer[] likesId);
    public int deleteLikeByLikeId(Integer likeId,Integer userId);
}
