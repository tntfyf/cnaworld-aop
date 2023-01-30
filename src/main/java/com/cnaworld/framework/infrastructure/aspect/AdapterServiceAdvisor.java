package com.cnaworld.framework.infrastructure.aspect;

import lombok.Getter;
import lombok.Setter;
import org.springframework.aop.Pointcut;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.AbstractBeanFactoryPointcutAdvisor;


/**
 * 动态配置切面
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Getter
@Setter
public class AdapterServiceAdvisor extends AbstractBeanFactoryPointcutAdvisor {

	/**
	 * 表达式
	 */
	private String expression;

	/**
	 * getPointcut
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @return Pointcut
	 */
	@Override
	public Pointcut getPointcut() {
		AspectJExpressionPointcut adapterPointcut = new AspectJExpressionPointcut();
		//从配置文件中获取PointCut表达式
		adapterPointcut.setExpression(expression);
		return adapterPointcut;
	}
}
