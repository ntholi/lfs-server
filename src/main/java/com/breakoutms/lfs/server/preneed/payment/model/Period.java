package com.breakoutms.lfs.server.preneed.payment.model;

import java.time.LocalDate;
import java.time.Month;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class Period {
	
	@NotNull
	@Column(columnDefinition = "TINYINT")
	@Convert(converter = MonthAttributeConverter.class)
	private Month month;
	
	@NotNull
	@Column(columnDefinition = "SMALLINT")
	private Integer year;
	
	public Period(@NotNull Integer month, @NotNull Integer year) {
		this.month = MonthAttributeConverter.fromInt(month);
		this.year = year;
	}
	
	public Period(@NotNull Month month, @NotNull Integer year) {
		this.month = month;
		this.year = year;
	}
	
	public Period() {
	}
	
	public static Period now() {
		return Period.of(LocalDate.now());
	}
	
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
	
	/**
	 * Calculate difference "minus" in months between two periods
	 * @param p1
	 * @param p2
	 * @return **/
	public static int differenceInMonths(Period p1, Period p2) {
		int years = p1.year - p2.year;
		int p1Months = (12 * years) + p1.month.getValue();
		int p2Months = p2.month.getValue();
		
		return p1Months - p2Months;
	}

	public String name() {
		return month+ " "+ year;
	}
	
	@Override
	public String toString() {
		return name();
	}
}