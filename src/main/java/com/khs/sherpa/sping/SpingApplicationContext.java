package com.khs.sherpa.sping;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.GenericBeanDefinition;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.khs.sherpa.context.ApplicationContext;
import com.khs.sherpa.context.factory.ManagedBeanFactory;
import com.khs.sherpa.exception.NoSuchManagedBeanExcpetion;

public class SpingApplicationContext implements ApplicationContext {

	public static final String SHERPA_APPLICATION_CONTEXT_ATTRIBUTE = SpingApplicationContext.class.getName() + ".CONTEXT";
	
	private ManagedBeanFactory managedBeanFactory;

	private Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	public SpingApplicationContext(ServletContext servletContext) {
		org.springframework.context.ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		if(!context.containsBean("managedBeanFactory")) {
			BeanDefinitionRegistry registry = ((BeanDefinitionRegistry)context.getAutowireCapableBeanFactory());
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(SpringManagedBeanFactory.class);
			registry.registerBeanDefinition("managedBeanFactory", beanDefinition);
		}
		
		managedBeanFactory = context.getBean(ManagedBeanFactory.class);
	}
	public boolean containsManagedBean(Class<?> type) {
		return managedBeanFactory.containsManagedBean(type);
	}

	public boolean containsManagedBean(String name) {
		return managedBeanFactory.containsManagedBean(name);
	}

	public <T> T getManagedBean(Class<T> type) throws NoSuchManagedBeanExcpetion {
		return managedBeanFactory.getManagedBean(type);
	}

	public Object getManagedBean(String name) throws NoSuchManagedBeanExcpetion {
		return managedBeanFactory.getManagedBean(name);
	}

	public <T> T getManagedBean(String name, Class<T> type) throws NoSuchManagedBeanExcpetion {
		return managedBeanFactory.getManagedBean(name, type);
	}

	public boolean isTypeMatch(String name, Class<?> type) throws NoSuchManagedBeanExcpetion {
		return managedBeanFactory.isTypeMatch(name, type);
	}

	public Class<?> getType(String name) throws NoSuchManagedBeanExcpetion {
		return managedBeanFactory.getType(name);
	}

	public void setAttribute(String key, Object value) {
		attributes.put(key, value);
	}
	
	public Object getAttribute(String key) {
		return attributes.get(key);
	}
	
	public ManagedBeanFactory getManagedBeanFactory() {
		return managedBeanFactory;
	}

	public static ApplicationContext getApplicationContext(ServletContext context) {
		return (ApplicationContext) context.getAttribute(SHERPA_APPLICATION_CONTEXT_ATTRIBUTE);
	}

	public Map<String, Object> getEndpointTypes() {
		return managedBeanFactory.getEndpointTypes();
	}
	public <T> Collection<T> getManagedBeans(Class<T> type) {
		return managedBeanFactory.getManagedBeans(type);
	}
}
