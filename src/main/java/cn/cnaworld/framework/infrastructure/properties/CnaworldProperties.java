package cn.cnaworld.framework.infrastructure.properties;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * cnaworld属性配置
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Configuration
@ConfigurationProperties(prefix="cnaworld")
@EnableConfigurationProperties(CnaworldProperties.class)
@Getter
@Setter
@ToString
public class CnaworldProperties {

    private CnaworldAopProperties aop;

    /**
     * aop配置
     * @author Lucifer
     * @date 2023/1/30
     * @since 1.0
     */
    @Getter
    @Setter
    @ToString
    public static class CnaworldAopProperties {

        /**
         * 总开关
         */
        private boolean enable;

        /**
         * 属性实体
         */
        private List<AopEntity> properties;

        /**
         * 属性实体
         * @author Lucifer
         * @date 2023/1/30
         * @since 1.0
         */
        @Getter
        @Setter
        @ToString
        public static class AopEntity {

            /**
             * 后置处理器开关
             */
            private boolean postProcessor=true;
            /**
             * 前置处理器开关
             */
            private boolean preProcessor=true;
            /**
             * 环绕处理器开关
             */
            private boolean aroundProcessor=true;
            /**
             * 异常处理器开关
             */
            private boolean errorProcessor=true;
            /**
             * 切点表达式
             */
            private String execution;

            /**
             * 实现类
             */
            private String processorClass="cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor";

            /**
             * 默认实现日志打印级别
             */
            private String logLevel = "debug";

        }

    }

}
