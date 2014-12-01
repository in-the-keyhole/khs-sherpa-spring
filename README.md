khs-sherpa-spring [![Build Status](https://secure.travis-ci.org/in-the-keyhole/khs-sherpa-spring.png?branch=master)](http://travis-ci.org/in-the-keyhole/khs-sherpa-spring)
=================

Integrates Sherpa restful JSON API framework with Spring and Spring authentication

Link to Sherpa project here...[https://github.com/in-the-keyhole/khs-sherpa] 

Getting Started
---------------
Validated with SPRING version 3.1.3-RELEASE 

Using Maven: add this dependency in your 'pom.xml' (available in Maven central repo). 

    <dependency>
   	 <groupId>com.keyholesoftware</groupId>
   	 <artifactId>khs-sherpa-spring</artifactId>
   	<version>1.2.1</version>
    </dependency>
   

To build it, clone then install in local maven repo:

    $ git clone ...
	$ cd khs-sherpa-spring
	$ mvn install

Add the following entry to  `sherpa.properties`:

	application.context=com.khs.sherpa.spring.SpringApplicationContext


Add the following to your `web.xml` after Sherpa's listener, but before your application listener:


	<listener>
	    <listener-class>com.khs.sherpa.spring.SpringApplicationContext</listener-class>
	</listener>


Your ready to start wiring things up with Spring and Sherpa
	

@Autowire Dependencies
----------------------

Allows Sherpa @Endpoints to @Autowire Spring beans


	@Endpoint
	public class UserEndpoint {
		
		@Autowired
		private UserInfoService userInfoService;
		
		@Action(mapping = "/service/users", method = MethodRequest.GET)
		public Collection<UserInfo> getUsers() {
		return userInfoService.getAll();
		}
	}

	 
Authenticate End points with Spring Security
--------------------------------------------

Add the spring application context entries shown below to the application context XML( this configures authentication against LDAP). 

    <bean class="com.khs.sherpa.spring.SpringAuthentication"/>
       
	<security:ldap-server id="contextSource"
		url="ldap://<host>:<port>/dc=keyholesoftware,dc=com" 
		manager-dn="cn=KeyholeRootUser,cn=Root DNs,cn=config" 
		manager-password="<password>" />

	<bean id="ldapTemplate" class="org.springframework.ldap.core.LdapTemplate">
		<constructor-arg ref="contextSource" />
	</bean>

	<security:authentication-manager alias="authManager">
		<security:ldap-authentication-provider 
			user-dn-pattern="uid={0},ou=Users"
			user-details-class="inetOrgPerson"
			group-search-filter="member={0}" 
			group-search-base="ou=Groups" 
			role-prefix="none"/>
	</security:authentication-manager>
	

Turn on endpoint authentication by adding the following entry to the sherpa.properties file

	#turn on endpoint authentication
	endpoint.authentication=true

Get authenticated token from Sherpa with URL below.

  token =  <host url>/sherpa?action=authenticate&userid="<userid>"&password="<password>"

 Invoke end point URL using jquery, notice auth token and userid being set in request header. 

	$.ajax({
	 url:sherpa/service/users,
     type:"GET",
     beforeSend: function (request)
     {   // add secure token an userid to request header
    	 var token = $("#token").val();
    	 var userid = $("#userid").val();
         request.setRequestHeader("token", token);
         request.setRequestHeader("userid", userid);
     },
      success: function(data) {
         $("#results").val(FormatJSON(data, ""));
      },
      error: function(jaXhr,status,errorThrown ) { 
         $("#results").val("Error executing ");
	  }
        
    }); 

