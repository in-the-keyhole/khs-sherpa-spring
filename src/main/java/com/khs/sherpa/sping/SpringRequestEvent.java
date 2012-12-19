package com.khs.sherpa.sping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.khs.sherpa.context.ApplicationContext;
import com.khs.sherpa.events.RequestEvent;

public class SpringRequestEvent implements RequestEvent {

	private static final Logger LOGGER = LoggerFactory.getLogger(SpringRequestEvent.class);
	
	public void before(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {
		if(LOGGER.isTraceEnabled()) {
			LOGGER.debug("Before request... ");
			LOGGER.debug("Session: " + request.getSession().getId());
			LOGGER.debug("Session timeout: " + request.getSession().getMaxInactiveInterval());
		}
		SecurityContext context = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT_KEY");
		if(context != null) {
			if(LOGGER.isDebugEnabled()) {
				LOGGER.debug("Loading... " + context);
			}
			SecurityContextHolder.setContext(context);
		}
		
	}

	public void after(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {

	}

}
