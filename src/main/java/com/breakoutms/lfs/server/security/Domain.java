package com.breakoutms.lfs.server.security;

import com.breakoutms.lfs.server.user.model.PrivilegeType;

public enum Domain {
	MORTUARY(Const.MORTUARY), 
	UNDERTAKER(Const.UNDERTAKER), 
	PRENEED(Const.PRENEED), 
	SALES(Const.SALES), 
	REVENUE(Const.REVENUE), 
	ADMIN(Const.ADMIN);
	
	private final String path;
	
	Domain(String str) {
		String low = name().toLowerCase();
		if(!str.equals(low)) {
			throw new IllegalArgumentException(str);
		}
		this.path = low;
	}
	
	public static class Const{
		public static final String MORTUARY = "mortuary";
		public static final String UNDERTAKER = "undertaker";
		public static final String PRENEED = "preneed";
		public static final String SALES = "sales";
		public static final String REVENUE = "revenue";
		public static final String ADMIN = "admin";
		
		private Const() {}
	}
	
	private String attribute = "hasAnyRole('%s', 'ADMIN') and hasAuthority('%s')";

	public String canRead() {
		return String.format(attribute, name(), PrivilegeType.READ);
	}
	public String canWrite() {
		return String.format(attribute, name(), PrivilegeType.WRITE);
	}
	public String canUpdate() {
		return String.format(attribute, name(), PrivilegeType.UPDATE);
	}
	public String canDelete() {
		return String.format(attribute, name(), PrivilegeType.DELETE);
	}
	public String antPattern() {
		return "/"+path+"/**";
	}
}
