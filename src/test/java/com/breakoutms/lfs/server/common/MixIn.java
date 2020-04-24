package com.breakoutms.lfs.server.common;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixIn {
	@JsonIgnore
	abstract int getBranch(); // we don't need it!
	@JsonIgnore
	abstract LocalDateTime getCreatedAt();
}