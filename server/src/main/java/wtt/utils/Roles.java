package wtt.utils;

import cn.hutool.extra.spring.SpringUtil;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Component;
import wtt.handler.server.RoleService;
import wtt.handler.server.serverImpl.AdminHandler;
import wtt.handler.server.serverImpl.SellerHandler;
import wtt.handler.server.serverImpl.UserHandler;

import java.util.HashMap;
import java.util.Map;

@Getter
@AllArgsConstructor
public enum Roles {
    ADMIN("admin", AdminHandler.class,"管理员"),
    USER("user", UserHandler.class,"普通用户"),
    SELLER("seller", SellerHandler.class,"商家");

    private String value;
    private Class<? extends RoleService> roleService ;
    private final static Map<Roles, Class<?>> STRATEGY_MAP=new HashMap<>();
    private String desc;
    static {
        for (Roles r:
                Roles.values()) {
            STRATEGY_MAP.put(r, r.getRoleService());
            System.out.println(STRATEGY_MAP.get(r));
        }
    }
    public RoleService get() {
        return (RoleService) SpringUtil.getBean(STRATEGY_MAP.get(this));
    }

    public Class<?> getClasses(){
        return STRATEGY_MAP.get(this);
    }
}
