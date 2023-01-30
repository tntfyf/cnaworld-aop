package cn.cnaworld.framework.infrastructure.processor;

import lombok.Data;

/**
 * aop处理上下文
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Data
public class CnaworldAopProcessorContext {

	/**
	 * aop处理接口
	 */
	private CnaworldAopProcessor cnaworldAopProcessor;
 
}
