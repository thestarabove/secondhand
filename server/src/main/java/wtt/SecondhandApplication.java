package wtt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import wtt.config.RedisMessageListenerContainerConfig;

@SpringBootApplication
@EnableTransactionManagement
@Import(RedisMessageListenerContainerConfig.class)
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true,exposeProxy = true)

public class SecondhandApplication {

    public static void main(String[] args) {
        SpringApplication.run(SecondhandApplication.class, args);
    }

}
