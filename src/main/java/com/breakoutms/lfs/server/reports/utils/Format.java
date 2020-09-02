package com.breakoutms.lfs.server.reports.utils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

public interface Format {

	
	public static String date(LocalDate date) {
		return date(date, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	}
	
	public static String date(LocalDateTime date) {
		return date(date, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
	}
	
	public static String date(TemporalAccessor date, DateTimeFormatter formatter) {
		if(date == null) {
			return null;
		}
		return formatter.format(date);
	}
}
