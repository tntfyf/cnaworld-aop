package cn.cnaworld.framework.infrastructure.config;

import cn.cnaworld.framework.infrastructure.properties.CnaworldProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.ConfigurableEnvironment;
/**
 * aop配置启动类
 * @author Lucifer
 * @date 2023/1/30
 * @since 1.0
 */
@Configuration
@EnableConfigurationProperties(CnaworldProperties.class)
@Order(Ordered.HIGHEST_PRECEDENCE)
public class CnaworldAopConfiguration {

	/**
	 * 启动自定义后置处理器，注入环境变量
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