package com.melilla.gestPlanes.envers;


import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;






public class CustomRevisionListener implements RevisionListener {
	// private static final org.apache.logging.log4j.Logger logg = org.apache.logging.log4j.LogManager.getLogger(LogExample.class);


	@Override
	public void newRevision(Object revisionEntity) {
		
		CustomRevisionEntity revision = (CustomRevisionEntity) revisionEntity;
		
		SecurityContext ctx =  SecurityContextHolder.getContext();
		
		Authentication auth = (Authentication) ctx.getAuthentication();
		
		WebAuthenticationDetails details = (WebAuthenticationDetails) auth.getDetails();
		
		String ipAddress = details.getRemoteAddress(); 
		
		revision.setUserName(auth.getName());
		revision.setIp(ipAddress);
		
		
	}

}
