package com.cnaworld.framework.infrastructure.aspect;

import com.cnaworld.framework.infrastructure.processor.CnaworldAopProcessorContext;
import lombok.Data;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 根据自定义实现处理切面逻辑
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Data
public class AdapterServiceMonitorInterceptor implements MethodInterceptor {

	/**
	 * aop处理逻辑上下文
	 */
	private CnaworldAopProcessorContext cnaworldAopProcessorContext;

	/**
	 *前置处理器开关
	 */
	private boolean preProcessor;

	/**
	 *异常处理器开关
	 */
	private boolean errorProcessor;

	/**
	 *后置处理器开关
	 */
	private boolean postProcessor;

	/**
	 *环绕处理器开关
	 */
	private boolean aroundProcessor;

	/**
	 * 日志等级
	 */
	private String logLevel;

	/**
	 * 代理调用
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @return 返回值
	 */
	@Override
	public Object invoke(MethodInvocation invocation) throws Throwable {
		// 开始时间
		long stime = System.currentTimeMillis();

		if(preProcessor) {
			//前置处理器
			cnaworldAopProcessorContext.getCnaworldAopProcessor().prePostProcessor(invocation,stime);
		}

		Object obj;

		long etime;

		try {
			obj =  invocation.proceed();
			// 结束时间
	        etime = System.currentTimeMillis();
		}catch (Exception e) {
			if(errorProcessor) {
				//异常处理器
				etime = System.currentTimeMillis();
				cnaworldAopProcessorContext.getCnaworldAopProcessor().errorProcessor(invocation,e,stime,etime);
			}
			throw e;
		}
		
		if(postProcessor) {
			//后置处理器
			obj= cnaworldAopProcessorContext.getCnaworldAopProcessor().postProcessor(invocation,obj,stime,etime);
		}

		if(aroundProcessor) {
			//环绕处理器
			obj = cnaworldAopProcessorContext.getCnaworldAopProcessor().aroundProcessor(invocation, obj, stime, etime);
		}
		return obj;
	}
	
}
