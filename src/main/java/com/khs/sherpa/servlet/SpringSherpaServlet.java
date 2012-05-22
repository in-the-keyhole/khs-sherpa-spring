package com.khs.sherpa.servlet;

import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.web.servlet.FrameworkServlet;

import com.khs.sherpa.json.service.ActivityService;
import com.khs.sherpa.json.service.DefaultActivityService;
import com.khs.sherpa.json.service.DefaultTokenService;
import com.khs.sherpa.json.service.JSONService;
import com.khs.sherpa.json.service.SessionStatus;
import com.khs.sherpa.json.service.SessionTokenService;
import com.khs.sherpa.json.service.SpringAuthentication;
import com.khs.sherpa.json.service.UserService;

public class SpringSherpaServlet extends FrameworkServlet {

//	private static Logger LOG = Logger.getLogger(SpringSherpaServlet.class.getName());
	
	private static final long serialVersionUID = -6712767422014510622L;

	private JSONService service = new JSONService();;
	private Settings settings = new Settings();
	
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionStatus sessionStatus = null;
		
		SherpaRequest sherpa = new SherpaRequest();
		sherpa.setService(service);
		sherpa.setSettings(settings);
		sherpa.setSessionStatus(sessionStatus);
		sherpa.loadRequest(request, response);
	
//		sherpa.setRoles("ROLE_USER");
		
		sherpa.setTarget(ReflectionCache.getObject(sherpa.getEndpoint()));
		sherpa.run();
		
	}

	@Override
	protected void initFrameworkServlet() throws ServletException {
		
		try {
			service.setUserService(getWebApplicationContext().getBean(UserService.class));
		} catch (NoSuchBeanDefinitionException e) {
			UserService userService = (UserService)
					getWebApplicationContext().getAutowireCapableBeanFactory().createBean(SpringAuthentication.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
			service.setUserService(userService);
		}
		
		try {
			service.setTokenService(getWebApplicationContext().getBean(SessionTokenService.class));
		} catch (NoSuchBeanDefinitionException e) {
			SessionTokenService tokenService = (SessionTokenService)
					getWebApplicationContext().getAutowireCapableBeanFactory().createBean(DefaultTokenService.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
			service.setTokenService(tokenService);
		}
		
		try {
			service.setActivityService(getWebApplicationContext().getBean(ActivityService.class));
		} catch (NoSuchBeanDefinitionException e) {
			ActivityService activityService = (ActivityService)
					getWebApplicationContext().getAutowireCapableBeanFactory().createBean(DefaultActivityService.class, AutowireCapableBeanFactory.AUTOWIRE_BY_TYPE, true);
			service.setActivityService(activityService);
		}
		
		Map<String, Object> endpoints = getWebApplicationContext().getBeansWithAnnotation(com.khs.sherpa.annotation.Endpoint.class);
		ReflectionCache.addObjects(endpoints);
		
	}
	
}
