package cn.cnaworld.framework.infrastructure.statics.constants;

/**
 * 日志等级
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
public class AopConstant {

    /**
     * 默认注解表达式
     */
    public static final String DEFAULT_EXECUTION = "@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)";

    /**
     * 属性配置名称
     */
    public static final String PROPERTIES = "cnaworld.aop.properties";

    /**
     * 后置处理器开关配置名称
     */
    public static final String POST_PROCESSOR = ".post-processor";

    /**
     * 后置处理器开关配置名称
     */
    public static final String POSTPROCESSOR = ".postProcessor";

    /**
     * 前置处理器开关配置名称
     */
    public static final String PRE_PROCESSOR = ".pre-processor";

    /**
     * 前置处理器开关配置名称
     */
    public static final String PREPROCESSOR = ".preProcessor";

    /**
     * 环绕处理器开关配置名称
     */
    public static final String AROUND_PROCESSOR = ".around-processor";

    /**
     * 环绕处理器开关配置名称
     */
    public static final String AROUNDPROCESSOR = ".aroundProcessor";

    /**
     * 异常处理器开关配置名称
     */
    public static final String ERROR_PROCESSOR = ".error-processor";

    /**
     * 异常处理器开关配置名称
     */
    public static final String ERRORPROCESSOR = ".errorProcessor";

    /**
     * 切面表达式配置名称
     */
    public static final String EXECUTION = ".execution";

    /**
     * 实现类配置名称
     */
    public static final String AOP_PROCESSOR = ".aop-processor";

    /**
     * 实现类配置名称
     */
    public static final String AOPPROCESSOR = ".aopProcessor";

    /**
     * 默认日志等级配置名称
     */
    public static final String LOG_LEVEL = ".log-level";

    /**
     * 默认日志等级配置名称
     */
    public static final String LOGLEVEL = ".logLevel";

}
