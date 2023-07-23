package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wtt.pojo.Likes;

import java.util.List;

@Mapper
public interface LikesMapper extends BaseMapper<Likes> {
    int deleteByLikesId(Integer likeId,Integer userId);

    int insertLikes(Likes record);

    //新增时，不覆盖原有数据
    int insertSelective(Likes record);

    Likes selectByLikesId(Integer likeId);

    List<Likes> selectAllLikesByUserId(Integer userId);
   Likes selectLikesByUserIdandGoodId(Integer userId,Integer secondHandId);

    List<Likes> findAllByPage(Integer userId,Integer pageNum,Integer pageSize);

    int updateByPrimaryKeySelective(Likes record);

    int updateByLikesId(Likes record);
}