package wtt.handler.server;

import cn.dev33.satoken.stp.StpInterface;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface RoleService   {
    public  List<String> doHandler();
    public  boolean validate();
}
