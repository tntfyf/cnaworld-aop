# 1.0.0版本

1. 支持配置的形式实现前置、后置、异常、环绕处理器

2. 提供默认Slf4j的aopLog实现，可使用@CnaAopLog 注解实现帮助实现aopLog,也可自定义注解

3. 客户端配置

   ```yaml
   #使用cnaworld
   cnaworld:
     #aop配置
     aop:
       #总开关
       enable: true 
       #切面列表
       properties:
         #配置切面表达式语法可以切包，方法等 根据业务开启环绕处理器和异常处理器
         - execution: "execution(* cn.cnaworld.framework..*.*Controller.*(..))"
           #前置处理器开关配置，默认为true 开启
           preProcessor: false
           #后置处理器开关配置，默认为true 开启
           postProcessor: false
         #配置注解方式支持配置注解切面 根据业务开启环绕处理器和异常处理器
         - execution: "@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)"
           processorClass: cn.cnaworld.framework.infrastructure.component.operatelog.CnoocAopOperateLogProcessor #自定义本地数据库实现
           #前置处理器开关配置，默认为true 开启
           preProcessor: false
           #后置处理器开关配置，默认为true 开启
           postProcessor: false
           #环绕处理器开关配置，默认为true 开启
           aroundProcessor: false
           #异常处理器开关配置，默认为true 开启
           errorProcessor: false
   ```

4.  实现 CbdfAopProcessor 接口

   ```java
   import org.aopalliance.intercept.MethodInvocation;
   import org.springframework.context.annotation.Configuration;
   
   import com.alibaba.fastjson.JSON;
   import com.cnaworld.cbdf.aop.infrastructure.processor.CbdfAopProcessor;
   
   import lombok.extern.slf4j.Slf4j;
   
   @Configuration
   public class CnaworldAopOperateLogProcessor extends CnaworldAopSlf4jProcessor{
   
       /**
        *前置处理器
        */
   	@Override
   	public void prePostProcessor(MethodInvocation invocation,long stime) {
   		// TODO  自定义业务实现 例如可以将日志存入数据库
   	}
   
       /**
        *后置处理器
        */
   	@Override
   	public Object postProcessor(MethodInvocation invocation, Object returnObject,long stime,long etime) {
   		// TODO  自定义业务实现 例如可以将日志存入数据库
   	}
   
       /**
        *异常处理器
        */
       @Override
       public void errorProcessor(MethodInvocation invocation, Exception e, long stime, long etime) {
           // TODO  自定义业务实现 例如可以将日志存入数据库
       }
   
       /**
        *环绕处理器
        */
       @Override
       public Object aroundProcessor(MethodInvocation invocation , Object returnObject, long stime, long etime) {
          // TODO  自定义业务实现 例如可以将日志存入数据库
       }
   	
   }
   ```
   
   

