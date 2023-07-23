package wtt.handler;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import java.util.Date;
@Component
@Slf4j
//时间自动填充
public class MyMetaObjectHandler implements MetaObjectHandler {
    @Override
    public void insertFill(MetaObject metaObject) {
        System.out.println("start insert fill.....");
         this.fillStrategy(metaObject,"createTime",new Date());
         //this.setFieldValByName("updateTime",new Date(),metaObject);
        this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
        log.info("end insert fill......");
    }
    @Override
    public void updateFill(MetaObject metaObject) {
        System.out.println("start update fill.....");
         this.strictUpdateFill(metaObject,"updateTime",Date.class,new Date());
        log.info("end update fill......");
    }
}
