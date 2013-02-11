package com.khs.sherpa.spring;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.RequestContextListener;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.khs.sherpa.context.ApplicationContext;
import com.khs.sherpa.events.RequestEvent;

public class SpringRequestEvent implements RequestEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringRequestEvent.class);
	
	private static final String REQUEST_ATTRIBUTES_ATTRIBUTE = RequestContextListener.class.getName() + ".REQUEST_ATTRIBUTES";
	
	public void before(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {
		SecurityContext context = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT_KEY");
		if(context != null) {
			SecurityContextHolder.setContext(context);
		}
		
		ServletRequestAttributes attributes = new ServletRequestAttributes(request);
		request.setAttribute(REQUEST_ATTRIBUTES_ATTRIBUTE, attributes);
		LocaleContextHolder.setLocale(request.getLocale());
		RequestContextHolder.setRequestAttributes(attributes);
		
	}

	public void after(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {

	}

}
