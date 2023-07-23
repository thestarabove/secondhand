package wtt.config;


import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
class MyMvcConfig implements WebMvcConfigurer {

//    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
//            "classpath:/META-INF/resources/", "classpath:/resources/",
//             "classpath:/public/" };


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

            registry.addResourceHandler("/**").addResourceLocations("file:D:\\3.2\\javaweb\\secondhand2\\static\\");

//        if (!registry.hasMappingForPattern("/**")) {
//            registry.addResourceHandler("/**").addResourceLocations(
//                    CLASSPATH_RESOURCE_LOCATIONS);
//        }

    }
}
