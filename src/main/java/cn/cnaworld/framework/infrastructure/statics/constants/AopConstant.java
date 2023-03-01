package cn.cnaworld.framework.infrastructure.statics.constants;

/**
 * 日志等级
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
public class AopConstant {

    /**
     * trace级别
     */
    public static final String TRACE = "trace";

    /**
     * debug级别
     */
    public static final String DEBUG = "debug";

    /**
     * info级别
     */
    public static final String INFO = "info";

    /**
     * warn级别
     */
    public static final String WARN = "warn";

    /**
     * error级别
     */
    public static final String ERROR = "error";

    /**
     * 默认注解表达式
     */
    public static final String DEFAULT_EXECUTION = "@annotation(cn.cnaworld.framework.infrastructure.annotation.CnaAopLog)";

    /**
     * 默认log实现
     */
    public static final String DEFAULT_LOGIMPL = "cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor";

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
    public static final String PROCESSOR_CLASS = ".processor-class";

    /**
     * 实现类配置名称
     */
    public static final String PROCESSORCLASS = ".processorClass";

    /**
     * 默认日志等级配置名称
     */
    public static final String LOG_LEVEL = ".log-level";

    /**
     * 默认日志等级配置名称
     */
    public static final String LOGLEVEL = ".logLevel";

}
