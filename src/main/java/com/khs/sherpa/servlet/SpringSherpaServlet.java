package com.khs.sherpa.servlet;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.FrameworkServlet;

import com.khs.sherpa.endpoint.SherpaEndpoint;
import com.khs.sherpa.json.service.JSONService;
import com.khs.sherpa.json.service.SessionStatus;
import com.khs.sherpa.parser.BooleanParamParser;
import com.khs.sherpa.parser.CalendarParamParser;
import com.khs.sherpa.parser.DateParamParser;
import com.khs.sherpa.parser.DoubleParamPaser;
import com.khs.sherpa.parser.FloatParamParser;
import com.khs.sherpa.parser.IntegerParamParser;
import com.khs.sherpa.parser.JsonParamParser;
import com.khs.sherpa.parser.ParamParser;
import com.khs.sherpa.parser.StringParamParser;
import com.khs.sherpa.util.SettingsContext;
import com.khs.sherpa.util.SettingsLoader;
import com.khs.sherpa.util.SpringSettingsLoader;

public class SpringSherpaServlet extends FrameworkServlet {

	
	private static final long serialVersionUID = -6712767422014510622L;

	private JSONService service = new JSONService();;
	
	@Override
	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SessionStatus sessionStatus = null;
		
		SherpaRequest sherpa = new SherpaRequest();
		sherpa.setService(service);
		sherpa.setSessionStatus(sessionStatus);
		sherpa.loadRequest(request, response);
	
		sherpa.setTarget(ReflectionCache.getObject(sherpa.getEndpoint()));
		sherpa.run();
		
	}

	@Override
	protected void initFrameworkServlet() throws ServletException {
		
		String configFile = "sherpa.properties";
		if(getInitParameter("sherpaConfigPath") != null) {
			configFile = getInitParameter("sherpaConfigPath");
		}
		
		SettingsLoader loader = new SpringSettingsLoader(configFile, getWebApplicationContext());
		
		// loading service
		service.setUserService(loader.userService());
		service.setTokenService(loader.tokenService());
		service.setActivityService(loader.activityService());
		
		// loading settings
		Settings settings = new Settings();
		settings.endpointPackage = loader.endpoint();
		settings.sessionTimeout = loader.timeout();
		settings.dateFormat = loader.dateFormat();
		settings.dateTimeFormat = loader.dateTimeFormat();
		settings.activityLogging = loader.logging();
		settings.encode = loader.encoding();
		settings.sherpaAdmin = loader.sherpaAdmin();
		SettingsContext context = new SettingsContext();
		context.setSettings(settings);
		
		// initialize parsers
		List<ParamParser<?>> parsers = new ArrayList<ParamParser<?>>();
		parsers.add(new StringParamParser());
		parsers.add(new IntegerParamParser());
		parsers.add(new DoubleParamPaser());
		parsers.add(new FloatParamParser());
		parsers.add(new BooleanParamParser());
		parsers.add(new DateParamParser());
		parsers.add(new CalendarParamParser());
		parsers.add(new JsonParamParser());
		service.setParsers(parsers);
		
		Map<String, Object> endpoints = getWebApplicationContext().getBeansWithAnnotation(com.khs.sherpa.annotation.Endpoint.class);
		ReflectionCache.addObjects(endpoints);
		
		// hard code sherpa endpoint
		ReflectionCache.addObject("sherpa", SherpaEndpoint.class);
	}
	
}
