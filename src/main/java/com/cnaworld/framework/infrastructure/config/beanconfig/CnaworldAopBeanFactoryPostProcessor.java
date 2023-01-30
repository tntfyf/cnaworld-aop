package com.cnaworld.framework.infrastructure.config.beanconfig;

import com.cnaworld.framework.infrastructure.aspect.AdapterServiceAdvisor;
import com.cnaworld.framework.infrastructure.aspect.AdapterServiceMonitorInterceptor;
import com.cnaworld.framework.infrastructure.processor.CnaworldAopProcessor;
import com.cnaworld.framework.infrastructure.processor.CnaworldAopProcessorContext;
import com.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor;
import com.cnaworld.framework.infrastructure.properties.CnaworldProperties;
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

import java.util.HashMap;
import java.util.Map;
/**
 * 自定义spring工厂后置处理器
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
	
	public CnaworldAopBeanFactoryPostProcessor(ConfigurableEnvironment environment){
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
	public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
		log.info("register CnaworldAopBeanFactoryPostProcessor start");
		//spring的bean工厂注册类实例化
		DefaultListableBeanFactory defaultListableBeanFactory = (DefaultListableBeanFactory) beanFactory;
		MutablePropertySources propertyValues =  environment.getPropertySources();

		Map<String, CnaworldProperties.CnaworldAopProperties.AopEntity> aopPropertiesMap = new HashMap<>();
		getAopProperties(propertyValues, aopPropertiesMap);

		if(ObjectUtils.isNotEmpty(aopPropertiesMap)) {
		    for (Map.Entry<String ,  CnaworldProperties.CnaworldAopProperties.AopEntity> entry : aopPropertiesMap.entrySet()) {
		    	String index = entry.getKey();
				CnaworldProperties.CnaworldAopProperties.AopEntity aopEntity = entry.getValue();
		        if(StringUtils.isBlank(aopEntity.getExecution())) {
					log.debug("cnaworld aop execution 解析失败");
					continue;
		        }

				String processorClassName="com.cnaworld.framework.infrastructure.processor.impl.CnaworldAopSlf4jProcessor";
				//切面实现类
				AdapterServiceMonitorInterceptor adapterServiceMonitorInterceptor=new AdapterServiceMonitorInterceptor();
				//aop上下文
				CnaworldAopProcessorContext cnaworldAopProcessorContext =new CnaworldAopProcessorContext();
				//实例化实现类
				Class<?> cbdfAopProcessorClass;
				if(StringUtils.isNotBlank(aopEntity.getProcessorClass())) {
					processorClassName= aopEntity.getProcessorClass();
				}

				try {
					cbdfAopProcessorClass = Class.forName(processorClassName);
				} catch (ClassNotFoundException e) {
					log.debug("cnaworld aop processorClass 解析失败");
					continue;
				}

				CnaworldAopProcessor cnaworldAopProcessor;

				try {
					Object obj  =   cbdfAopProcessorClass.newInstance();
					if (!cbdfAopProcessorClass.isInstance(obj)) {
						log.debug("cnaworld aop processorClass 解析失败");
						continue;
					}
					cnaworldAopProcessor =(CnaworldAopProcessor) obj;
					if (cnaworldAopProcessor instanceof CnaworldAopSlf4jProcessor){
						((CnaworldAopSlf4jProcessor) cnaworldAopProcessor).setLogLevel(aopEntity.getLogLevel());
					}
				} catch (InstantiationException | IllegalAccessException e) {
					log.debug("cnaworld aop processorClass 解析失败");
					continue;
				}
				//将实现类及开关装入上下文中
				cnaworldAopProcessorContext.setCnaworldAopProcessor(cnaworldAopProcessor);
				adapterServiceMonitorInterceptor.setCnaworldAopProcessorContext(cnaworldAopProcessorContext);
				adapterServiceMonitorInterceptor.setAroundProcessor(aopEntity.isAroundProcessor());
				adapterServiceMonitorInterceptor.setErrorProcessor(aopEntity.isErrorProcessor());
				adapterServiceMonitorInterceptor.setPostProcessor(aopEntity.isPostProcessor());
				adapterServiceMonitorInterceptor.setPreProcessor(aopEntity.isPreProcessor());

				String adviceBeanName="adapterServiceAdvice"+index;
				//注册Bean定义，容器根据定义返回bean
				BeanDefinitionBuilder beanDefinitionBuilder
				= BeanDefinitionBuilder.genericBeanDefinition(AdapterServiceAdvisor.class);
				BeanDefinition beanDefinition = beanDefinitionBuilder.getRawBeanDefinition();
				beanDefinition.getPropertyValues().add("expression", aopEntity.getExecution());
				beanDefinition.getPropertyValues().add("adviceBeanName",adviceBeanName);
				beanDefinition.getPropertyValues().add("advice",adapterServiceMonitorInterceptor);
				beanDefinition.getPropertyValues().add("order",Ordered.LOWEST_PRECEDENCE);
				defaultListableBeanFactory.registerBeanDefinition(adviceBeanName, beanDefinition);
		    }
		}

		log.info("register CnaworldAopBeanFactoryPostProcessor finish");
	}
	/**
	 * 获取配置文件中的属性值
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param propertyValues 全量属性信息
	 * @param aopPropertiesMap 过滤后的属性容器
	 */
	private void getAopProperties(MutablePropertySources propertyValues, Map<String,  CnaworldProperties.CnaworldAopProperties.AopEntity> aopPropertiesMap) {
		//获取环境变量中的配置信息
		for(PropertySource<?> pv: propertyValues) {
			PropertySource<?> propertySource = propertyValues.get(pv.getName());
			if (propertySource instanceof MapPropertySource){
				String[] propertyNames = ((MapPropertySource) propertySource).getPropertyNames();

				for (String propertyName : propertyNames) {
					if (propertyName.contains("cnaworld.aop.properties")) {
						String index = propertyName.substring(propertyName.indexOf("["), propertyName.indexOf("]"));
						CnaworldProperties.CnaworldAopProperties.AopEntity aopEntity;
						if (aopPropertiesMap.containsKey(index)) {
							aopEntity = aopPropertiesMap.get(index);
						} else {
							aopEntity = new CnaworldProperties.CnaworldAopProperties.AopEntity();
						}

						if (propertyName.contains(".postProcessor")) {
							Object value = propertySource.getProperty(propertyName);
							if (value != null) {
								aopEntity.setPostProcessor((boolean) value);
							}
						}
						if (propertyName.contains(".preProcessor")) {
							Object value = propertySource.getProperty(propertyName);
							if (value != null) {
								aopEntity.setPreProcessor((boolean) value);
							}
						}
						if (propertyName.contains(".aroundProcessor")) {
							Object value = propertySource.getProperty(propertyName);
							if (value != null) {
								aopEntity.setAroundProcessor((boolean) value);
							}
						}
						if (propertyName.contains(".errorProcessor")) {
							Object value = propertySource.getProperty(propertyName);
							if (value != null) {
								aopEntity.setErrorProcessor((boolean) value);
							}
						}

						if (propertyName.contains(".execution")) {
							Object value = propertySource.getProperty(propertyName);
							aopEntity.setExecution((String) value);
						}
						if (propertyName.contains(".processorClass")) {
							Object value = propertySource.getProperty(propertyName);
							aopEntity.setProcessorClass((String) value);
						}
						if (propertyName.contains(".logLevel")) {
							Object value = propertySource.getProperty(propertyName);
							aopEntity.setLogLevel((String) value);
						}
						aopPropertiesMap.put(index, aopEntity);
					}
				}
			}
		}
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}
	
}
