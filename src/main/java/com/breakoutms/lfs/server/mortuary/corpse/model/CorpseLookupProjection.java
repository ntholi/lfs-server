package com.breakoutms.lfs.server.mortuary.corpse.model;

import java.time.LocalDateTime;

public interface CorpseLookupProjection {
	
	public String getTagNo();
	public String getNames();
	public String getSurname();
	public LocalDateTime getArrivalDate();
}
