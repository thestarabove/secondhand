package wtt.handler.factoryHandler;


import org.springframework.stereotype.Service;
import wtt.utils.Roles;

import java.util.HashMap;
import java.util.Map;

@Service
public class Factory {
    private final static Map<String, Object> stringHandlerMap=new HashMap<>();

    public static Object getInvokeStrategy(String name){
        return  stringHandlerMap.get(name);
    }
    public static void register(Roles name, Object handler){
        if (name.getValue().equals("")|| handler==null){
            return;
        }
        stringHandlerMap.put(name.getValue(),handler);
    }
}
