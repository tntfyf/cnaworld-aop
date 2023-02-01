#Spring boot 快速实现 aop ：无需了解 spring aop 技术简单配置下yml开箱即用！
# 1.0.2版本 

作用：
1. 支持yml配置的形式实现前置、后置、异常、环绕处理器，将业务与配置搭建解耦，降低spring aop 学习成本，快速实现功能。

2. 提供基于Slf4j的Log实现cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor，根据日志等级log-level 打印切点方法入参，返参，响应时间，异常信息。

3. 内置注解@CnaAopLog 可直接配置到方法上实现AOP，采用默认实现。若在配置文件中有相同execution配置项，则以配置文件为准，配置项为：@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)

4. 默认实现暂不支持MultipartFile类型入参

5. 客户端配置

   pom.xml 引入依赖

   ```
   <dependency>
       <groupId>cn.cnaworld.framework</groupId>
       <artifactId>aop</artifactId>
       <version>1.0.2</version>
   </dependency>
   ```

   application.yml 新增配置

   ```yaml
   #使用cnaworld.aop生效最少配置，可对方法使用@CnaAopLog注解观察现象
   cnaworld:
     #aop配置
     aop:
       #总开关
       enable: true
   ```

   ```yaml
   #使用cnaworld.aop生效完整配置
   cnaworld:
     #aop配置
     aop:
       #总开关
       enable: true
       #切面列表
       properties:
         #配置切面表达式语法可以切包，方法等 根据业务开启环绕处理器和异常处理器
         - execution: "execution(* cn.cnaworld.framework..*.*Controller.*(..))"
           #默认logback实现 可不配置
           processor-class: cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor
           #默认logback实现日志等级默认为debug级别
           log-level: debug
           #前置处理器开关配置，默认为true 开启
           pre-processor: true
           #后置处理器开关配置，默认为true 开启
           post-processor: true
           #异常处理器开关配置，默认为true 开启
           error-processor: true
           #环绕处理器开关配置，默认为true 开启
           around-processor: true
           #配置注解方式支持配置注解切面，可使用自定义注解 根据业务开启环绕处理器和异常处理器
         - execution: "@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)"
           #自定义本地数据库实现
           processor-class: cn.cnaworld.framework.infrastructure.component.operatelog.CnoocAopOperateLogProcessor
           #前置处理器开关关闭
           pre-processor: false
           #后置处理器开关关闭
           post-processor: false
   
   ```

6. 效果演示

   ```xml
   2023-02-01 18:31:46.393  INFO 13536 --- [nio-8080-exec-3] c.c.f.i.p.i.CnaworldAopSlf4jProcessor    : 前置处理器|方法名：cn.cnaworld.cnaworldaoptest.api.TestApi.test,入参：["text"]
   业务输出:text
   
   2023-02-01 18:31:46.418  INFO 13536 --- [nio-8080-exec-3] c.c.f.i.p.i.CnaworldAopSlf4jProcessor    : 后置处理器|方法名：cn.cnaworld.cnaworldaoptest.api.TestApi.test,入参：["text"],反参："return : text"
   
   2023-02-01 18:31:46.418  INFO 13536 --- [nio-8080-exec-3] c.c.f.i.p.i.CnaworldAopSlf4jProcessor    : 环绕处理器|方法名：cn.cnaworld.cnaworldaoptest.api.TestApi.test,执行时间：81毫秒,入参：["text"],返参："return : text"
   ```

7. 自定义逻辑需实现 CnaworldAopProcessor接口,并配置到配置项processor-class 中

   ```
   import org.aopalliance.intercept.MethodInvocation;
   import org.springframework.context.annotation.Configuration;
   
   import com.alibaba.fastjson.JSON;
   import com.cnaworld.cbdf.aop.infrastructure.processor.CbdfAopProcessor;
   
   import lombok.extern.slf4j.Slf4j;
   
   @Configuration
   public class CnaworldAopOperateLogProcessor implements CnaworldAopProcessor{
   
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

8. 若不生效可启动类配置包扫描 cn.cnaworld.framework

   ```java
   @SpringBootApplication
   @ComponentScan({"cn.cnaworld.cnaworldaoptest","cn.cnaworld.framework"})
   public class CnaworldAopTestApplication {
   
       public static void main(String[] args) {
           SpringApplication.run(CnaworldAopTestApplication.class, args);
       }
   }
   ```
