package wtt.Log;


import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME )
@Target({ElementType.METHOD})
@Documented
public @interface LogMethod {
}
