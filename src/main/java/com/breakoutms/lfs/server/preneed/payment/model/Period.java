package com.breakoutms.lfs.server.preneed.payment.model;

import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Embeddable
public class Period implements Comparable<Period>{

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

	public static Period fromOrdinal(String period) {
		if(period == null || period.isBlank()) {
			return null;
		}
		String date = new StringBuilder(period)
				.insert(2, "-")
				.append("-01")
				.toString();

		return Period.of(LocalDate.parse(date, 
				DateTimeFormatter.ofPattern("yy-MM-dd")));	
	} 

	public Period next() {
		return plusMonths(1);
	}

	public Period previous() {
		return minusMonths(1);
	}

	public Period minusMonths(int months) {
		LocalDate date = LocalDate.of(year, month, 1)
				.minusMonths(months);
		return Period.of(date);
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
		LocalDate date1 = LocalDate.of(p1.year, p1.month, 1); 
		LocalDate date2 = LocalDate.of(p2.year, p2.month, 1); 

		return (int) ChronoUnit.MONTHS.between(date1, date2);
	}

	public String name() {
		return month+ " "+ year;
	}

	public Integer ordinal() {
		if(year == null && month == null) {
			return null;
		}
		String y = String.valueOf(year).substring(2);
		String m = String.format("%02d", month.getValue());
		return Integer.valueOf(y + m);
	}

	@Override
	public String toString() {
		return name();
	}

	@Override
	public int compareTo(Period o) {
		return ordinal().compareTo(o.ordinal());
	}
}