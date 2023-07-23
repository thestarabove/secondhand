package wtt.config;

import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//拦截器
@Configuration
public class MyBatisConfig {
    @Bean
    public MybatisPlusInterceptor plusInterceptor(){
         MybatisPlusInterceptor interceptor=new MybatisPlusInterceptor();
         interceptor.addInnerInterceptor(new PaginationInnerInterceptor());
         return interceptor;
    }
}
