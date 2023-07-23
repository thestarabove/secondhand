package wtt.OptinalTest;

import org.checkerframework.checker.units.qual.A;
import org.junit.jupiter.api.Test;
import org.junit.platform.commons.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import wtt.pojo.Goods;
import wtt.pojo.User;
import wtt.service.UserService;
import wtt.utils.OptionalNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SpringBootTest
public class OptionalTest1 {
    @Autowired
    OptionalNull optionalNull;
    @Autowired
    UserService userService;
    @Test
    public void test1(){
        List<String> list=new ArrayList<>();
        list.add("11111");
        Optional<List<String>> nullList = optionalNull.isNullList(list);
        nullList.filter(strings ->!strings.get(0).equals("111")).ifPresent(strings -> System.out.println(strings.get(0)+"xxxxxxxxx"));

    }
    @Test
    public void test3(){
        List<String> list=new ArrayList<>();
        list.add("");
        Optional<List<String>> nullList = optionalNull.isNullList(list);
        nullList.ifPresent(strings -> System.out.println(strings.get(0)+"xxxxxxxxx"));
    }
    @Test
    public void test2(){
        User user=new User();
        user.setUserName("xiLin");
        user.setPassword("xxx");
        Optional<User> user1=optionalNull.isNullClass(user);
  //      user1.filter(user2 -> StringUtils.isNotBlank(user2.getPassword())).ifPresent(user2 -> System.out.println(user2.getPassword()));
     //   System.out.println(user.getPassword().length()+"xxxxxx");
//        User user2 = user1.orElseGet(() -> new User());
//        System.out.println(user2);
//        Goods goods=new Goods();
//        Optional<Goods> goods1=optionalNull.isNullClass(goods);
//        Optional<List<String>> strings = goods1.map(goods2 -> goods2.getCartImg());
//        strings.ifPresent(strings1 -> System.out.println("xxxxx"));
        user.setUserId(30);
        Optional<User> optionalUser=optionalNull.isNullClass(user);
        User user2 = optionalUser.orElseGet(new Supplier<User>() {
            @Override
            public User get() {
                return userService.updateUser(user);
            }
        });
        System.out.println(user2);
    }
}
