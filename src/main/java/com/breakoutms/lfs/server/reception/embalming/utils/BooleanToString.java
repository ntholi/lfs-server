package com.breakoutms.lfs.server.reception.embalming.utils;

import org.apache.commons.lang3.StringUtils;

public class BooleanToString {

	private static final String NONE = "None";
	private static final String PRESENT = "Present";
	
    public static String booleanToString(final Boolean bool) {
        if(bool == null) {
        	return NONE;
        }
        return bool.booleanValue()? PRESENT : NONE;
    }
    
    public static Boolean stringToBoolean(final String str) {
    	if(StringUtils.isBlank(str)) {
    		return false;
    	}
    	return str.equals(PRESENT);
    }
}
