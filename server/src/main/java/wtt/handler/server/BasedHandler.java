package wtt.handler.server;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wtt.handler.factoryHandler.Factory;
import wtt.utils.Roles;

import java.util.List;

@Service
public  abstract  class BasedHandler implements InitializingBean, RoleService {
    @Autowired
   private Factory factory;
    @Override
    public void afterPropertiesSet() throws Exception {
        factory.register(getRole(),this);
    }

    public abstract Roles getRole();
    public abstract List<String> doHandler();
    public abstract boolean validate();
}
