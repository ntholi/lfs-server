package com.breakoutms.lfs.server.audit;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.val;
import lombok.extern.log4j.Log4j2;

@Log4j2
public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		AuditRevisionInfo rev = (AuditRevisionInfo) revisionEntity;
		if(isAuthenticationObjectNull()) {
			return;
		}
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			val userId = ((UserDetails)principal).getUsername();
			if(StringUtils.isNumeric(userId)) {
				rev.setUserId(Integer.valueOf(userId));
			}
		}
	}

	private boolean isAuthenticationObjectNull() {
		if(SecurityContextHolder.getContext() == null) {
			log.error("Unable to add Audit info because SecurityContextHolder.getContext() is null");
			return true;
		}
		if(SecurityContextHolder.getContext().getAuthentication() == null) {
			log.error("Unable to add Audit info because SecurityContextHolder.getContext().getAuthentication() is null");
			return true;
		}
		if(SecurityContextHolder.getContext().getAuthentication().getPrincipal() == null) {
			log.error("Unable to add Audit info because SecurityContextHolder.getContext().getAuthentication().getPrincipal() is null");
			return true;
		}
		return false;
	}

}
