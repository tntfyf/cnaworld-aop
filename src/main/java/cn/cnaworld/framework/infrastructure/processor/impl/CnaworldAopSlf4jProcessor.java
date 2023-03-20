package cn.cnaworld.framework.infrastructure.processor.impl;

import cn.cnaworld.framework.infrastructure.processor.CnaworldAopProcessor;
import cn.cnaworld.framework.infrastructure.statics.enums.LogLevel;
import cn.cnaworld.framework.infrastructure.utils.log.CnaLogUtil;
import com.alibaba.fastjson.JSON;
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

	private LogLevel logLevel = LogLevel.INFO;

	public void setLogLevel(LogLevel logLevel) {
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
		printlnLog(logLevel,"前置处理器|方法名：{},入参：{}",getMethodName(invocation),getArgumentString(invocation));
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
		printlnLog(logLevel,"后置处理器|方法名：{},入参：{},反参：{}",getMethodName(invocation),getArgumentString(invocation),JSON.toJSONString(returnObject));
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
		printlnLog(logLevel,"异常处理器|方法名：{},入参：{},异常：",getMethodName(invocation),getArgumentString(invocation),e);
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
		printlnLog(logLevel,"环绕处理器|方法名：{},执行时间：{}毫秒,入参：{},返参：{}"
				,getMethodName(invocation),etime-stime
				,getArgumentString(invocation),JSON.toJSONString(returnObject));
		return returnObject;
	}

	/**
	 * 生成全限定方法名称
	 *
	 * @param invocation 调用信息
	 * @return String 全限定方法名称
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
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

	/**
	 * 根据日志等级打印日志
	 * @author Lucifer
	 * @date 2023/2/1
	 * @since 1.0.1
	 * @param logLevel 日志等级
	 * @param message 日志内容
	 * @param arguments 参数
	 */
	private void printlnLog(LogLevel logLevel, String message, Object... arguments){
		switch (logLevel) {
			case TRACE:
				CnaLogUtil.trace(log,message,arguments);
				break;
			case DEBUG:
				CnaLogUtil.debug(log,message,arguments);
				break;
			case INFO:
				CnaLogUtil.info(log,message,arguments);
				break;
			case WARN:
				CnaLogUtil.warn(log,message,arguments);
				break;
			case ERROR:
				CnaLogUtil.error(log,message,arguments);
				break;
			default:
				CnaLogUtil.info(log,message,arguments);
				break;
		}
	}

}
