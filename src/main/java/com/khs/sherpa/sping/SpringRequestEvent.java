package com.khs.sherpa.sping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.khs.sherpa.context.ApplicationContext;
import com.khs.sherpa.events.RequestEvent;

public class SpringRequestEvent implements RequestEvent {

	public void before(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {
		SecurityContext context = (SecurityContext) request.getSession().getAttribute("SPRING_SECURITY_CONTEXT_KEY");
		if(context != null) {
			SecurityContextHolder.setContext(context);
		}
		
	}

	public void after(ApplicationContext applicationContext, HttpServletRequest request, HttpServletResponse response) {

	}

}
