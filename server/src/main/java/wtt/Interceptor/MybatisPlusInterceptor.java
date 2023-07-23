package wtt.Interceptor;


import org.apache.ibatis.executor.parameter.ParameterHandler;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.plugin.*;
import wtt.pojo.User;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Intercepts({
        @Signature(type = StatementHandler.class, method = "prepare", args = {Connection.class, Integer.class})
})
public class MybatisPlusInterceptor implements Interceptor {
    @Override
    public Object intercept(Invocation invocation) throws Throwable {
        StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
        ParameterHandler parameterHandler = statementHandler.getParameterHandler();
        BoundSql boundSql = statementHandler.getBoundSql();
        String sql = boundSql.getSql();
        Object parameterObject = parameterHandler.getParameterObject();
        String sqlType=parseSqlType(sql);
        //System.out.println(sqlType+"---------------------sql");
        //System.out.println(parameterObject+"---------------------------pp");
        Map<String,Object> map=toParamMap(parameterObject);
        // 在这里可以获取到 SQL 语句和参数值，进行相应的处理
       if (sqlType.equals("INSERT")){
           System.out.println(sqlType+"---------------------INSERT");

           System.out.println(parameterObject+"----------------------------parameter");
       }
        if (sqlType.equals("UPDATE")){
            System.out.println(sqlType+"---------------------UPDATE");
            User user= (User) map.get("param1");
            System.out.println(user+"----------------------------parameter");
        }
        if (sqlType.equals("DELETE")){
            System.out.println(sqlType+"---------------------DELETE");

            System.out.println(parameterObject+"----------------------------parameter");
        }
        return invocation.proceed();
    }
    @Override
    public Object plugin(Object target) {
        return Plugin.wrap(target, this);
    }

    @Override
    public void setProperties(Properties properties) {
        // 可以在这里设置一些属性或配置
    }
    private Map<String,Object> toParamMap(Object parameterObject) throws NoSuchFieldException,IllegalAccessException{
        Map<String,Object> paramMap=new HashMap<>();
        if (parameterObject == null) {
            return paramMap;
        }
        if (parameterObject instanceof Map){
            // 如果参数对象本身就是 Map 类型，则直接返回
            paramMap.putAll((Map<String, Object>) parameterObject);
            return paramMap;
        }
        // 使用反射获取参数对象的字段和值
        Class<?> parameterClass=parameterObject.getClass();
        Field[] fields=parameterClass.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Object value = field.get(parameterObject);
            paramMap.put(field.getName(), value);
        }
        return paramMap;
    }
   private String parseSqlType(String sql){
       String trimmedSql=sql.trim().toLowerCase();
       if (trimmedSql.startsWith("select")){
           return "SELECT";
       }else if (trimmedSql.startsWith("insert")) {
           return "INSERT";
       } else if (trimmedSql.startsWith("update")) {
           return "UPDATE";
       } else if (trimmedSql.startsWith("delete")) {
           return "DELETE";
       } else {
           // 其他类型的 SQL 语句
           return "UNKNOWN";
       }
   }

}
