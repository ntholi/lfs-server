package com.breakoutms.lfs.server.common;

import com.breakoutms.lfs.server.branch.Branch;

public interface UnitTest {

	public default Branch getBranch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return branch;
	}
	
}
