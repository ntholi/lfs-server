package com.breakoutms.lfs.server.preneed.payment.model;

import java.time.LocalDate;
import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class Period {
	
	@NotNull
	@Column(nullable=false, columnDefinition = "TINYINT UNSIGNED")
	private Month month;
	
	@NotNull
	@Column(nullable=false, columnDefinition = "SMALLINT UNSIGNED")
	private Integer year;
	
	public static Period of(LocalDate date) {
		return Period.of(date.getYear(), date.getMonth());
	}
	
	public static Period of(Integer year, Month month) {
		Period period = new Period();
		period.year = year;
		period.month = month;
		return period;
	}
	
	public Period next() {
		return plusMonths(1);
	}
	
	public Period plusMonths(int months) {
		months = month.getValue() + months;
		int years = 0;
		if(months > 12){
			years = Math.floorDiv(months, 12);
		}
		int leftOver = months - (years * 12);
		return Period.of(year+years, Month.of(leftOver));
	}
	
	public boolean isBefore(Period period) {
		if(year.intValue() < period.year.intValue()) {
			return true;
		}
		else if((year.intValue() == period.year.intValue())
				&& (month.getValue() < period.month.getValue())) 
			return true;
		return false;
	}
	
	public boolean isAfter(Period period) {
		if(year.intValue() > period.year.intValue()) {
			return true;
		}
		else if((year.intValue() == period.year.intValue())
				&& (month.getValue() > period.month.getValue()))
			return true;
		return false;
	}

	public String name() {
		return month+ " "+ year;
	}
}