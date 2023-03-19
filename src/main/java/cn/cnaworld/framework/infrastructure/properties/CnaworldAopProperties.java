package cn.cnaworld.framework.infrastructure.properties;

import cn.cnaworld.framework.infrastructure.statics.constants.AopConstant;
import cn.cnaworld.framework.infrastructure.statics.enums.LogLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

/**
 * cnaworld属性配置
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */

@Getter
@Setter
@ToString
@ConfigurationProperties(prefix="cnaworld.aop")
public class CnaworldAopProperties {

    /**
     * 默认实现开关
     */
    private boolean defaultEnable = true;

    /**
     * 属性实体
     */
    private List<AopProperties> properties;

    /**
     * 属性实体
     * @author Lucifer
     * @date 2023/1/30
     * @since 1.0
     */
    @Getter
    @Setter
    @ToString
    public static class AopProperties {

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
        private String execution=AopConstant.DEFAULT_EXECUTION;

        /**
         * 实现类
         */
        private String processorClass=AopConstant.DEFAULT_LOGIMPL;

        /**
         * 默认实现日志打印级别
         */
        private LogLevel logLevel = LogLevel.INFO;

    }

}
