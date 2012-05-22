package com.khs.sherpa.util;

import java.util.Properties;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.context.WebApplicationContext;

import com.khs.sherpa.json.service.ActivityService;
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
