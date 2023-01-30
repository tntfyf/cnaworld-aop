package com.cnaworld.framework.infrastructure.processor.impl;

import com.alibaba.fastjson.JSON;
import com.cnaworld.framework.infrastructure.processor.CnaworldAopProcessor;
import com.cnaworld.framework.infrastructure.statics.constants.LogLevelConstant;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.intercept.MethodInvocation;

import java.util.Arrays;

/**
 * 默认日志实现
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Slf4j
public class CnaworldAopSlf4jProcessor implements CnaworldAopProcessor {

	private String logLevel = LogLevelConstant.DEBUG;

	public void setLogLevel(String logLevel) {
		this.logLevel = logLevel;
	}

	/**
	 * 前置处理器根据日志等级打印API日志
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param stime 开始时间
	 */
	@Override
	public void prePostProcessor(MethodInvocation invocation,long stime) {
		if(LogLevelConstant.DEBUG.equalsIgnoreCase(logLevel)){
			log.debug("前置处理器|方法名：{},入参：{}",getMethodName(invocation),getArgumentString(invocation));
		}else if(LogLevelConstant.INFO.equalsIgnoreCase(logLevel)){
			log.info("前置处理器|方法名：{},入参：{}",getMethodName(invocation),getArgumentString(invocation));
		}else {
			log.error("cnooc-aop 默认实现仅支持debug或info日志等级 当前配置为："+logLevel);
		}
	}

	/**
	 * 后置处理器打印日志
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param returnObject 返回值
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return Object 处理后返回结果
	 */
	@Override
	public Object postProcessor(MethodInvocation invocation, Object returnObject,long stime,long etime) {
		if(LogLevelConstant.DEBUG.equalsIgnoreCase(logLevel)){
			log.debug("后置处理器|方法名：{},反参：{}",getMethodName(invocation),JSON.toJSONString(returnObject));
		}else if(LogLevelConstant.INFO.equalsIgnoreCase(logLevel)){
			log.info("后置处理器|方法名：{},反参：{}",getMethodName(invocation),JSON.toJSONString(returnObject));
		}else {
			log.error("cnooc-aop 默认实现仅支持debug或info日志等级 当前配置为："+logLevel);
		}
		return returnObject;
	}

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
	@Override
	public void errorProcessor(MethodInvocation invocation, Exception e,long stime,long etime) {
		log.error("异常处理器|方法名：{},入参：{},异常：{}",getMethodName(invocation),getArgumentString(invocation),e.getStackTrace());
	}

	/**
	 * 环绕处理器
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @param returnObject 返回结果
	 * @param stime 开始时间
	 * @param etime 结束时间
	 * @return Object 处理后返回结果
	 */
	@Override
	public Object aroundProcessor(MethodInvocation invocation, Object returnObject,long stime,long etime) {

		if(LogLevelConstant.DEBUG.equalsIgnoreCase(logLevel)){
			log.debug("环绕处理器|方法名：{},执行时间：{}毫秒,入参：{},返参：{}"
					,getMethodName(invocation),etime-stime
					,getArgumentString(invocation),JSON.toJSONString(returnObject));
		}else if(LogLevelConstant.INFO.equalsIgnoreCase(logLevel)){
			log.info("环绕处理器|方法名：{},执行时间：{}毫秒,入参：{},返参：{}"
					,getMethodName(invocation),etime-stime
					,getArgumentString(invocation),JSON.toJSONString(returnObject));
		}else {
			log.error("cnooc-aop 默认实现仅支持debug或info日志等级 当前配置为："+logLevel);
		}
		return returnObject;
	}

	/**
	 * 生成全限定方法名称
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @return String 全限定方法名称
	 */
	protected String getMethodName(MethodInvocation invocation) {
		return invocation.getMethod().getDeclaringClass().getName()
				+ "." + invocation.getMethod().getName();
	}

	/**
	 * 将参数转换成json 。 文件上传服务暂未兼容
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param invocation 调用信息
	 * @return String 参数json 或 参数内存地址
	 */
	protected String getArgumentString(MethodInvocation invocation) {
		Object[] argumentObject = invocation.getArguments();
		String argumentString = null;
		if (argumentObject.length > 0) {
			try {
				argumentString=JSON.toJSONString(argumentObject);
			}catch (Exception e){
				argumentString=Arrays.toString(argumentObject);
			}
		}
		return argumentString;
	}

}
