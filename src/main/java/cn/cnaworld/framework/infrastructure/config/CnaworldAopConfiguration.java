package cn.cnaworld.framework.infrastructure.config;

import cn.cnaworld.framework.infrastructure.properties.CnaworldAopProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.ConfigurableEnvironment;
/**
 * aop配置启动类
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties({CnaworldAopProperties.class})
public class CnaworldAopConfiguration {

	/**
	 * 启动自定义前置处理器，注入环境变量
	 * @author Lucifer
	 * @date 2023/1/30
	 * @since 1.0
	 * @param environment 环境变量
	 */
	@Bean
	public static CnaworldAopBeanFactoryPostProcessor cnoocAopLogBeanFactoryPostProcessor(ConfigurableEnvironment environment){
		return new CnaworldAopBeanFactoryPostProcessor(environment);
	}
    
}