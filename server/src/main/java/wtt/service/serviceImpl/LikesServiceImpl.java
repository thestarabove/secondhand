package wtt.service.serviceImpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtt.dao.LikesMapper;
import wtt.pojo.Likes;
import wtt.service.LikesService;
import wtt.utils.ComFoundException;

import java.util.List;
@Service("LikesService")
public class LikesServiceImpl implements LikesService {
    @Autowired
    private LikesMapper likesMapper;

    @Override
    public List<Likes> getAllLikesByUser(Integer userId) {
        List<Likes> likesList=likesMapper.selectAllLikesByUserId(userId);
        if (likesList == null||likesList.size()==0) {
            throw new ComFoundException("该用户未收藏任何商品！");
        }
        return likesList;
    }

    @Override
    public List<Likes> findAllByPage(Integer userId,Integer pageNum, Integer pageSize) {
        pageNum=(pageNum-1)*pageSize;

            List<Likes> likesList=likesMapper.findAllByPage(userId,pageNum,pageSize);
            if (likesList==null){
                throw new ComFoundException("分页查询失败！");
            }
        System.out.println(likesList+"......like");
        return likesList;
    }

    @Override
    public boolean getLikeByGood(Integer goodId, Integer userId) {
     Likes likes=likesMapper.selectLikesByUserIdandGoodId(userId,goodId);
        boolean flag=false;
           if (likes!=null) {
               flag = true;
           }
        return flag;
    }

    @Override
    public Likes getLikeByLikeId(Integer likeId) {
        Likes likes=likesMapper.selectByLikesId(likeId);
        if (likes==null){
            throw new ComFoundException();
        }
        return likes;
    }

    @Override
    public int addLikeByGoodId(Likes likes) {
        int flag=likesMapper.insertLikes(likes);
        return flag;
    }

    @Override
    public int deleteLikesByLikesId(Integer[] likesId) {

        return 0;
    }

    @Override
    public int deleteLikeByLikeId(Integer likeId,Integer userId) {
        int flag=likesMapper.deleteByLikesId(likeId,userId);
        return flag;
    }
}
