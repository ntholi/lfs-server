package com.breakoutms.lfs.server.preneed.policy.report;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class PlanTypeSummaryReport {
	
	private static final int currentYear = LocalDate.now().getYear();
	private String name;
	private Long count;
	@JsonIgnore private double year;
	
	public Double getAvgAge() {
		return ((double)currentYear) - year;
	}
}
