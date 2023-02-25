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


}
