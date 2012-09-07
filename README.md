khs-sherpa-spring
=================

Spring support for khsSherpa framework remote java object JSON data framework

About
-----

Turn Java application servers into a remote JSON data access mechanism for mobile and HTML 5/Java Script applications. 

This lightweight server side framework allows Java classes contained inside a JEE application sever
to become JSON end points that can be consumed via HTTP by native mobile devices or HTML/Javascript clients. 

Many MVC frameworks exist, but khsSherpa is intended to allow access to server side java objects with HTTP/and JSON. It 
also, provides session support for client applications that exist outside of a browser.

For complete usage and documentation checkout the core project on GitHub... 

    https://github.com/in-the-keyhole/khs-sherpa
    
Features  
--------
 * Annotation Based Configuration
 * Authentication and Role based permissions 
 * Session Support 
 * Plug-gable User Activity Logging
 * Type mapping
 * XSS prevention support
 * Works with any JEE application server
          
Getting Started
---------------
To build it clone then use Maven:

    $ git clone ...
	$ cd khs-sherpa-spring
	$ mvn install

Using Maven: add this dependency in your 'pom.xml' (available in Maven central repo)

    <dependency>
   	 <groupId>com.keyholesoftware</groupId>
   	 <artifactId>khs-sherpa-spring</artifactId>
   	<version>1.0.0</version>
    </dependency    
    
Configuring WEB.XML
-------------------
Add the khsSherpa framework jar to your classpath/maven dependency list and add the 
SherpaServlet to the WEB-INF/web.xml as shown below. 

    <servlet>	
  		<servlet-name>sherpa</servlet-name>
		<display-name>sherpa</display-name>
		<servlet-class>com.khs.sherpa.servlet.SherpaServlet</servlet-class>	
	</servlet>
	<servlet-mapping>
		<servlet-name>sherpa</servlet-name>
		<url-pattern>/sherpa</url-pattern>
	</servlet-mapping>


Spring Application Context Support
----------------------------------
Sherpa end points annotations and configuration elements can be configured and scanned 
using Spring context component scan directives. The example below shows how define a package
scan for Sherpa endpoints.
 
	<context:component-scan base-package="khs.example.endpoint">
		<context:include-filter type="annotation" expression="com.khs.sherpa.annotation.Endpoint"/>
	</context:component-scan>
 
Spring Security Support
----------------------- 


 