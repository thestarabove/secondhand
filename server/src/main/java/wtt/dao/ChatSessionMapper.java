package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import wtt.pojo.ChatSession;
import wtt.vo.ChatSessionVo;

import java.util.List;

@Mapper
public interface ChatSessionMapper extends BaseMapper<ChatSession> {
    List<ChatSessionVo> selectSessionList(@Param("userId") Integer userId);
}
