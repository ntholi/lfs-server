package lfs.server.common;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class MixIn {
	@JsonIgnore
	abstract int getBranch(); // we don't need it!
}