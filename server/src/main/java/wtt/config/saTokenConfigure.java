package wtt.config;

import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.router.SaRouter;
import cn.dev33.satoken.stp.StpUtil;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class saTokenConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
//         // 注册路由拦截器，自定义认证规则
//        registry.addInterceptor(new SaInterceptor(handler ->{
//                    // 登录认证 -- 拦截所有路由，并排除/user/doLogin 用于开放登录
//              SaRouter.match("/secondhand/**","/secondhand/user/doLogin",r -> StpUtil.checkLogin());
//                    // 角色认证 -- 拦截以 admin 开头的路由，必须具备 admin 角色或者 super-admin 角色才可以通过认证
//              SaRouter.match("/secondhand/admin/**",r->StpUtil.checkRole("3"));
//                    // 权限认证 -- 不同模块认证不同权限
//            SaRouter.match("/secondhand/user/**",r->StpUtil.checkRoleOr("0","1","2","3"));
//            SaRouter.match("/secondhand/user/seller/**",r->StpUtil.checkRole("2"));
//
//            SaRouter.match("/**", r -> System.out.println("--------权限认证成功-------"));
//
//        }).isAnnotation(true))
//                //拦截所有接口
//                .addPathPatterns("/secondhand/**")
//             //不拦截/user/doLogin登录接口
//                .excludePathPatterns("/secondhand/user/doLogin")
//                .excludePathPatterns("/secondhand/user/register")
//             //不拦截验证码
//                .excludePathPatterns("/secondhand/getKaptchaImage")
//                .excludePathPatterns("/secondhand/user/seller/uploadImage");
//        WebMvcConfigurer.super.addInterceptors(registry);
   }
}
