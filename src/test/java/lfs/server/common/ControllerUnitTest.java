package lfs.server.common;

import lfs.server.branch.Branch;

public interface ControllerUnitTest {

	public default Branch getBranch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return branch;
	}
}
