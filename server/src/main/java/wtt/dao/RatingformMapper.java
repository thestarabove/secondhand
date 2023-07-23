package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import wtt.pojo.Ratingform;

import java.util.List;

@Mapper
public interface RatingformMapper extends BaseMapper<Ratingform> {
    int deleteEvaluateId(Integer evaluateId);

    boolean insertRatingform(Ratingform record);

    int insertSelective(Ratingform record);

    Ratingform selectByEvaluateId(Integer evaluateId);

    List<Ratingform> selectAllByUserIdRatingforms(Integer userId);

    int updateByPrimaryKeySelective(Ratingform record);

    int updateByEvaluateId(Ratingform record);
}