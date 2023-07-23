package wtt.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;
import wtt.pojo.User;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
