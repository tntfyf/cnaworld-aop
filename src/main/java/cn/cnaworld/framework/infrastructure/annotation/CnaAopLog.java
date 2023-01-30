package cn.cnaworld.framework.infrastructure.annotation;

import cn.cnaworld.framework.infrastructure.statics.enums.OperationType;
import cn.cnaworld.framework.infrastructure.statics.enums.UserType;

import java.lang.annotation.*;

/**
 * 自定义操作日志记录注解
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0.0
 */
@Target({ElementType.PARAMETER, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface CnaAopLog {

    /**
     * 模块中文名称
     */
    String moduleName();

    /**
     * 操作名称
     */
    String operationName();

    /**
     * 操作类型
     */
    OperationType operationType();

    /**
     * 操作描述
     */
    UserType userType() default UserType.OTHER;

    /**
     * 是否保存请求的参数
     */
    boolean isSaveRequestData() default true;
}

