package wtt.utils;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

@Component
public class OptionalNull {
    //返回ListOptional判断不为空
    public <T>Optional<List<T>> isNullList(List<T> list){
       return Optional.ofNullable(list);
    }
    public <K,V>Optional<Map<K,V>> isNullMap(Map<K,V> map){
        return Optional.ofNullable(map);
    }
    //返回对象Optional判断不为空
    public <T>Optional<T> isNullClass(T t){
        return Optional.ofNullable(t);
    }
}
