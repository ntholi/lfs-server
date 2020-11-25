package com.breakoutms.lfs.server.mortuary.postmortem.model;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;

public interface PostmortemProjection {

	Integer getId();
	
	@Value("#{target.getCorpse().getTagNo()}")
	String getTagNo();
	
	LocalDateTime getDate();
	
	@Value("#{target.getPostmortemRequest().getLocation()}")
	String getLocation();
}
