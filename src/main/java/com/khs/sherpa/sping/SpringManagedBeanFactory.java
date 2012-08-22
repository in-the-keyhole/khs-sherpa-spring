package com.khs.sherpa.sping;

import java.util.Collection;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.core.token.TokenService;

import com.khs.sherpa.SherpaSettings;
import com.khs.sherpa.annotation.Endpoint;
import com.khs.sherpa.context.factory.InitManageBeanFactory;
import com.khs.sherpa.context.factory.ManagedBeanFactory;
import com.khs.sherpa.exception.NoSuchManagedBeanExcpetion;
import com.khs.sherpa.json.service.ActivityService;
import com.khs.sherpa.json.service.JsonProvider;
import com.khs.sherpa.json.service.UserService;

public class SpringManagedBeanFactory implements ManagedBeanFactory, InitManageBeanFactory, ApplicationContextAware {

	private ApplicationContext springApplicationContext;
	
	public boolean containsManagedBean(Class<?> type) {
		return springApplicationContext.containsBean(type.getSimpleName());
	}

	public boolean containsManagedBean(String name) {
		return springApplicationContext.containsBean(name);
	}

	public <T> T getManagedBean(Class<T> type) throws NoSuchManagedBeanExcpetion {
		return springApplicationContext.getBean(type);
	}

	public Object getManagedBean(String name) throws NoSuchManagedBeanExcpetion {
		return springApplicationContext.getBean(name);
	}

	public <T> T getManagedBean(String name, Class<T> type) throws NoSuchManagedBeanExcpetion {
		return springApplicationContext.getBean(name, type);
	}

	public boolean isTypeMatch(String name, Class<?> type) throws NoSuchManagedBeanExcpetion {
		return springApplicationContext.isTypeMatch(name, type);
	}

	public Class<?> getType(String name) throws NoSuchManagedBeanExcpetion {
		return springApplicationContext.getType(name);
	}

	public Map<String, Object> getEndpointTypes() {
		return springApplicationContext.getBeansWithAnnotation(Endpoint.class);
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.springApplicationContext = applicationContext;
	}

	public void init(SherpaSettings settings, ServletContext context) {
		BeanDefinitionRegistry registry = ((BeanDefinitionRegistry)springApplicationContext.getAutowireCapableBeanFactory());
		try {
			springApplicationContext.getBean(UserService.class);
		} catch (NoSuchBeanDefinitionException e) {
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(settings.userService());
			registry.registerBeanDefinition("userService", beanDefinition);
		}
		
		try {
			springApplicationContext.getBean(TokenService.class);
		} catch (NoSuchBeanDefinitionException e) {
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(settings.tokenService());
			registry.registerBeanDefinition("tokenService", beanDefinition);
		}

		try {
			springApplicationContext.getBean(ActivityService.class);
		} catch (NoSuchBeanDefinitionException e) {
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(settings.activityService());
			registry.registerBeanDefinition("activityService", beanDefinition);
		}
		
		try {
			springApplicationContext.getBean(JsonProvider.class);
		} catch (NoSuchBeanDefinitionException e) {
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(settings.jsonProvider());
			registry.registerBeanDefinition("jsonProvider", beanDefinition);
		}
		
		// load the root domain
//		this.loadManagedBeans("com.khs.sherpa.endpoint");		
	}
}
