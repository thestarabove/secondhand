package wtt.Log;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
@Aspect
@Component
@Slf4j
public class LogAop {

    @Pointcut("@annotation(wtt.Log.LogMethod)")
    public void MethodLog(){
    }

    @Before("MethodLog()")
    public void doBefore(JoinPoint joinPoint){
     //记录方法入参
      log.info("Request Method:{} ,Request Param:{}",joinPoint.getSignature(), JSON.toJSONString(joinPoint.getArgs()));
    }

    @AfterReturning(returning = "o",pointcut = "MethodLog()")
    public void AfterReturning(Object o){
   //记录方法返回
        log.info("Response Result:{}", JSONObject.toJSONString(o));
    }
}
