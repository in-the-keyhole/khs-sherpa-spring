package com.khs.sherpa.spring;

/*
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

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
import com.khs.sherpa.spring.SpringApplicationContext;
import com.khs.sherpa.spring.SpringManagedBeanFactory;

public class SpringApplicationContext implements ApplicationContext {

	public static final String SHERPA_APPLICATION_CONTEXT_ATTRIBUTE = SpringApplicationContext.class.getName() + ".CONTEXT";
	
	private ManagedBeanFactory managedBeanFactory;

	private Map<String, Object> attributes = new LinkedHashMap<String, Object>();

	public SpringApplicationContext(ServletContext servletContext) {
		org.springframework.context.ApplicationContext context = WebApplicationContextUtils.getRequiredWebApplicationContext(servletContext);

		if(!context.containsBean("managedBeanFactory")) {
			BeanDefinitionRegistry registry = ((BeanDefinitionRegistry)context.getAutowireCapableBeanFactory());
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(SpringManagedBeanFactory.class);
			registry.registerBeanDefinition("managedBeanFactory", beanDefinition);
		}
		
		managedBeanFactory = context.getBean(ManagedBeanFactory.class);
		{
			BeanDefinitionRegistry registry = ((BeanDefinitionRegistry)context.getAutowireCapableBeanFactory());
			GenericBeanDefinition beanDefinition = new GenericBeanDefinition();
			beanDefinition.setBeanClass(SpringRequestEvent.class);
			registry.registerBeanDefinition(SpringRequestEvent.class.getCanonicalName(), beanDefinition);
		}
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
