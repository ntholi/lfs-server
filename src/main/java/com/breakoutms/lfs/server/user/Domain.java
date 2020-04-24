package com.breakoutms.lfs.server.user;

public enum Domain {
	MORTUARY(Const.MORTUARY), 
	UNDERTAKER(Const.UNDERTAKER), 
	PRENEED(Const.PRENEED), 
	SALES(Const.SALES), 
	REVENUE(Const.REVENUE), 
	ADMIN(Const.ADMIN);
	
	Domain(String s) {
		String en = name().toLowerCase();
		if(!s.equals(en)) {
			throw new IllegalArgumentException(s);
		}
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
		return "/"+name().toLowerCase()+"/**";
	}
}
