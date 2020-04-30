package com.breakoutms.lfs.server.user.model;

public enum PrivilegeType {
	READ, 
	WRITE, 
	UPDATE, 
	DELETE,
	VIEW_REPORTS;
	
	public static class Can {
		private Can() {}
		public static final String READ = "hasAuthority('READ')";
		public static final String WRITE = "hasAuthority('WRITE')";
		public static final String UPDATE = "hasAuthority('UPDATE')";
		public static final String DELETE = "hasAuthority('DELETE')";
		public static final String VIEW_REPORTS = "hasAuthority('VIEW_REPORTS')";
    }
}
