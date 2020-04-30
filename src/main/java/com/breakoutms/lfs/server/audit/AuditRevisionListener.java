package com.breakoutms.lfs.server.audit;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.RevisionListener;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.val;

public class AuditRevisionListener implements RevisionListener {

	@Override
	public void newRevision(Object revisionEntity) {
		AuditRevisionInfo rev = (AuditRevisionInfo) revisionEntity;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if (principal instanceof UserDetails) {
			val userId = ((UserDetails)principal).getUsername();
			if(StringUtils.isNumeric(userId)) {
				rev.setUserId(Integer.valueOf(userId));
			}
		}
	}

}
