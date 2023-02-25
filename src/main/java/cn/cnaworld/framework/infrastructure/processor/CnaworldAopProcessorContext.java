package cn.cnaworld.framework.infrastructure.processor;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * aop处理上下文
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CnaworldAopProcessorContext {

	/**
	 * aop处理接口
	 */
	private CnaworldAopProcessor cnaworldAopProcessor;
 
}
