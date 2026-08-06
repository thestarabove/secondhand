package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wtt.pojo.ChatMessage;
import wtt.vo.ChatMessageVo;

import java.util.List;

@Mapper
public interface ChatMessageMapper extends BaseMapper<ChatMessage> {
    List<ChatMessageVo> selectMessagesBySession(@Param("sessionId") Long sessionId);

    int updateReadBySession(@Param("sessionId") Long sessionId, @Param("userId") Integer userId);
}
