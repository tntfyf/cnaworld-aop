package com.cnaworld.framework.infrastructure.processor;

import org.aopalliance.intercept.MethodInvocation;
/**
 * aop处理接口
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
public interface CnaworldAopProcessor {
	/**
	 * 前置处理器
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param stime 开始时间
	 */
	void prePostProcessor(MethodInvocation invocation,long stime);

	/**
	 * 后置处理器
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param returnObject 结果
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return Object 处理后返回结果
	 */
	Object postProcessor(MethodInvocation invocation,Object returnObject,long stime,long etime);

	/**
	 * 异常处理器
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param e 异常信息
	 * @param stime 开始时间
	 * @param etime 结束时间
	 */
	void errorProcessor(MethodInvocation invocation,Exception e,long stime,long etime);

	/**
	 * 环绕处理器
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param returnObject 原返回结果
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return Object 处理后返回结果
	 */
	Object aroundProcessor(MethodInvocation invocation,Object returnObject,long stime,long etime);

}
