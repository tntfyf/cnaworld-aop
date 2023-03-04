package cn.cnaworld.framework.infrastructure.config;

import cn.cnaworld.framework.infrastructure.aspect.AdapterServiceAdvisor;
import cn.cnaworld.framework.infrastructure.aspect.AdapterServiceMonitorInterceptor;
import cn.cnaworld.framework.infrastructure.processor.CnaworldAopProcessor;
import cn.cnaworld.framework.infrastructure.processor.CnaworldAopProcessorContext;
import cn.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor;
import cn.cnaworld.framework.infrastructure.properties.CnaworldAopProperties;
import cn.cnaworld.framework.infrastructure.statics.constants.AopConstant;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 * 自定义spring工厂前置处理器
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Slf4j
public class CnaworldAopBeanFactoryPostProcessor implements BeanFactoryPostProcessor,Ordered {
	/**
	 * spring 环境变量配置
	 */
	private final ConfigurableEnvironment environment;

	public CnaworldAopBeanFactoryPostProcessor( final ConfigurableEnvironment environment){
		this.environment = environment;
	}
	/**
	 * 将自定义aop切面添加到springBeanFactory中注册
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param beanFactory bean工厂
	 */
	@Override
	public void postProcessBeanFactory(  ConfigurableListableBeanFactory beanFactory) throws BeansException {
		log.info("cna-aop register CnaworldAopBeanFactoryPostProcessor start");
		//spring的bean工厂注册类实例化
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		MutablePropertySources propertyValues =  environment.getPropertySources();

		Map<String, CnaworldAopProperties.AopProperties.AopEntity> aopPropertiesMap = new HashMap<>();
		getAopProperties(propertyValues, aopPropertiesMap);
		if(ObjectUtils.isNotEmpty(aopPropertiesMap)) {
		    for (Map.Entry<String ,  CnaworldAopProperties.AopProperties.AopEntity> entry : aopPropertiesMap.entrySet()) {
		    	String index = entry.getKey();
				CnaworldAopProperties.AopProperties.AopEntity aopEntity = entry.getValue();
		        if(StringUtils.isBlank(aopEntity.getExecution())) {
					log.error("cnaworld aop execution can not empty ！");
					continue;
		        }

				//实例化实现类
				Class<?> cbdfAopProcessorClass;
				try {
					cbdfAopProcessorClass = Class.forName(aopEntity.getProcessorClass());
				} catch (ClassNotFoundException e) {
					log.error("cnaworld aop processorClass 解析失败 ：{}" , aopEntity.getProcessorClass());
					continue;
				}

				//aop上下文
				CnaworldAopProcessor cnaworldAopProcessor;

				try {
					Object obj  =   cbdfAopProcessorClass.newInstance();
					if (!cbdfAopProcessorClass.isInstance(obj)) {
						log.error("cnaworld aop processorClass 解析失败 ：{}" , aopEntity.getProcessorClass());
						continue;
					}
					cnaworldAopProcessor =(CnaworldAopProcessor) obj;
					if (cnaworldAopProcessor instanceof CnaworldAopSlf4jProcessor){
						((CnaworldAopSlf4jProcessor) cnaworldAopProcessor).setLogLevel(aopEntity.getLogLevel());
					}
				} catch (InstantiationException | IllegalAccessException e) {
					log.error("cnaworld aop processorClass 解析失败 ：{}" , aopEntity.getProcessorClass());
					continue;
				}

				//将实现类及开关装入上下文中
				//切面实现类
				AdapterServiceMonitorInterceptor adapterServiceMonitorInterceptor=new AdapterServiceMonitorInterceptor(new CnaworldAopProcessorContext(cnaworldAopProcessor),aopEntity.isPreProcessor(),aopEntity.isPostProcessor(),aopEntity.isAroundProcessor(),aopEntity.isErrorProcessor());
				String adviceBeanName="cna-aop "+index;
				//注册Bean定义，容器根据定义返回bean
				BeanDefinitionBuilder beanDefinitionBuilder
				= BeanDefinitionBuilder.genericBeanDefinition(AdapterServiceAdvisor.class);
				BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
				beanDefinition.getPropertyValues().add("expression", aopEntity.getExecution());
				beanDefinition.getPropertyValues().add("adviceBeanName",adviceBeanName);
				beanDefinition.getPropertyValues().add("advice",adapterServiceMonitorInterceptor);
				beanDefinition.setRole(2);
				log.info("cna-aop register {} success",aopEntity.getExecution());
				defaultListableBeanFactory.registerBeanDefinition(adviceBeanName, beanDefinition);
		    }
		}

		log.info("cna-aop register CnaworldAopBeanFactoryPostProcessor finish");
	}
	/**
	 * 获取配置文件中的属性值
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param propertyValues 全量属性信息
	 * @param aopPropertiesMap 过滤后的属性容器
	 */
	private void getAopProperties(MutablePropertySources propertyValues, Map<String,  CnaworldAopProperties.AopProperties.AopEntity> aopPropertiesMap) {
		//客户端是否覆盖默认配置 true 覆盖，false 不覆盖
		boolean defaultCnaworldAopProcessorFlag=false;
		//1、开关开启用户没配置是false 2、开关关闭 false
		boolean defaultEnabled=true;
		List<String> removeIndex = new ArrayList<>();
		for(PropertySource<?> pv: propertyValues) {
			PropertySource<?> propertySource = propertyValues.get(pv.getName());
			if (propertySource instanceof MapPropertySource){
				String[] propertyNames = ((MapPropertySource) propertySource).getPropertyNames();
				//优先获取客户端开关配置
				for (String propertyName : propertyNames) {
					if (propertyName.contains("cnaworld.aop.defaultEnable") || propertyName.contains("cnaworld.aop.default-enable")) {
						Object value = propertySource.getProperty(propertyName);
						Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop default-enable cannot be empty");
						defaultEnabled=(boolean) value;
					}
				}
				//注入客户端配置
				defaultCnaworldAopProcessorFlag = getClientAopProperties(aopPropertiesMap, defaultEnabled, removeIndex, propertySource, propertyNames);
			}
		}

		if (ObjectUtils.isNotEmpty(removeIndex)){
			for(String key:removeIndex){
				aopPropertiesMap.remove(key);
			}
		}

		//开关开启且用户没有配置过
		if (defaultEnabled && !defaultCnaworldAopProcessorFlag) {
			aopPropertiesMap.put("defaultCnaworldAopProcessor",new CnaworldAopProperties.AopProperties.AopEntity());
		}

	}

	/**
	 *  注入客户端其他配置
	 * @author Lucifer
	 * @date 2023/3/1
	 * @since 1.0.6
	 * @param aopPropertiesMap 即将注入spring的bean属性集合容器
	 * @param defaultEnabled 1、开关开启用户没配置是false 2、开关关闭 false 3、开启默认配置true
	 * @param removeIndex defaultEnabled开关关闭，移除所有默认配置,包括客户端自定义
	 * @param propertySource 配置源
	 * @param propertyNames  属性名
	 * @return defaultCnaworldAopProcessorFlag 客户端是否覆盖默认配置 true 覆盖，false 不覆盖
	 */
	private static boolean getClientAopProperties(Map<String, CnaworldAopProperties.AopProperties.AopEntity> aopPropertiesMap , boolean defaultEnabled, List<String> removeIndex, PropertySource<?> propertySource, String[] propertyNames) {
		//客户端是否覆盖默认配置 true 覆盖，false 不覆盖
		boolean defaultCnaworldAopProcessorFlag=false;

		for (String propertyName : propertyNames) {
			if (propertyName.contains(AopConstant.PROPERTIES)) {
				String index = propertyName.substring(propertyName.indexOf("[")+1, propertyName.indexOf("]"));
				CnaworldAopProperties.AopProperties.AopEntity aopEntity;
				if (aopPropertiesMap.containsKey(index)) {
					aopEntity = aopPropertiesMap.get(index);
				} else {
					aopEntity = new CnaworldAopProperties.AopProperties.AopEntity();
				}

				if (propertyName.contains(AopConstant.POST_PROCESSOR) || propertyName.contains(AopConstant.POSTPROCESSOR)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop post-processor cannot be empty");
					aopEntity.setPostProcessor((boolean) value);
				}
				if (propertyName.contains(AopConstant.PRE_PROCESSOR) || propertyName.contains(AopConstant.PREPROCESSOR)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop pre-processor cannot be empty");
					aopEntity.setPreProcessor((boolean) value);
				}
				if (propertyName.contains(AopConstant.AROUND_PROCESSOR) || propertyName.contains(AopConstant.AROUNDPROCESSOR)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop around-processor cannot be empty");
					aopEntity.setAroundProcessor((boolean) value);
				}
				if (propertyName.contains(AopConstant.ERROR_PROCESSOR) || propertyName.contains(AopConstant.ERRORPROCESSOR)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop error-processor cannot be empty");
					aopEntity.setErrorProcessor((boolean) value);
				}
				if (propertyName.contains(AopConstant.EXECUTION)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop execution cannot be empty");
					if (AopConstant.DEFAULT_EXECUTION.equals(value)) {
						if (!defaultEnabled){
							removeIndex.add(index);
						}else {
							defaultCnaworldAopProcessorFlag =true;
						}
					}
					aopEntity.setExecution((String) value);
				}
				if (propertyName.contains(AopConstant.PROCESSOR_CLASS) || propertyName.contains(AopConstant.PROCESSORCLASS)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop processor-class cannot be empty");
					aopEntity.setProcessorClass((String) value);
				}
				if (propertyName.contains(AopConstant.LOG_LEVEL) || propertyName.contains(AopConstant.LOGLEVEL)) {
					Object value = propertySource.getProperty(propertyName);
					Assert.isTrue(ObjectUtils.isNotEmpty(value),"cnaworld.aop log-level cannot be empty");
					String logLevel = (String) value;
					boolean logLevelBoolean= AopConstant.DEBUG.equalsIgnoreCase(logLevel)
							|| AopConstant.INFO.equalsIgnoreCase(logLevel)
							|| AopConstant.TRACE.equalsIgnoreCase(logLevel)
							|| AopConstant.ERROR.equalsIgnoreCase(logLevel)
							|| AopConstant.WARN.equalsIgnoreCase(logLevel);
					Assert.isTrue(logLevelBoolean,"cnaworld.aop log-level is misconfigured");
					aopEntity.setLogLevel(logLevel);
				}
				aopPropertiesMap.put(index, aopEntity);
			}
		}
		return defaultCnaworldAopProcessorFlag;
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}
	
}
