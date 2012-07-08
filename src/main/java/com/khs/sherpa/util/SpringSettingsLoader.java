package com.khs.sherpa.util;

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

import java.util.Properties;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;

import com.khs.sherpa.json.service.ActivityService;
import com.khs.sherpa.json.service.JsonProvider;
import com.khs.sherpa.json.service.SessionTokenService;
import com.khs.sherpa.json.service.SpringAuthentication;
import com.khs.sherpa.json.service.UserService;

public class SpringSettingsLoader extends SettingsLoader {

	protected WebApplicationContext ctx;
	
	public SpringSettingsLoader(String configFile, WebApplicationContext ctx) {
		super(configFile);
		this.ctx = ctx;
	}

	public SpringSettingsLoader(Properties properties, WebApplicationContext ctx) {
		super(properties);
		this.ctx = ctx;
	}

	@Override
	public String endpoint() {
		// not need for spring
		return null;
	}

	@Override
	public UserService userService() {
		try {
			return ctx.getBean(UserService.class);
		} catch (NoSuchBeanDefinitionException e) {
			String userClazzName = properties.getProperty("user.service");
			if (userClazzName == null) {
				return (UserService) createInstance(SpringAuthentication.class.getName());
			} else {
				return (UserService) createInstance(userClazzName);
			}
		}
	}

	@Override
	public JsonProvider jsonProvider() {
		try {
			return ctx.getBean(JsonProvider.class);
		} catch (Exception e) {
			return super.jsonProvider();
		}
		
	}

	@Override
	public SessionTokenService tokenService() {
		try {
			return ctx.getBean(SessionTokenService.class);
		} catch (NoSuchBeanDefinitionException e) {
			return super.tokenService();
		}
	}

	@Override
	public ActivityService activityService() {
		try {
			return ctx.getBean(ActivityService.class);
		} catch (NoSuchBeanDefinitionException e) {
			return super.activityService();
		}
	}

	// override so we user spring to create object. This will allow autowire to work.
	@Override
	protected Object createInstance(String name) {
		return ctx.getAutowireCapableBeanFactory().createBean(SpringAuthentication.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
	}

}
