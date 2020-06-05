package com.breakoutms.lfs.server.preneed.payment;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;
import java.time.Month;

import org.junit.jupiter.api.Test;

import com.breakoutms.lfs.server.preneed.payment.model.Period;

class PeriodTests {

	private LocalDate today = LocalDate.now();
	
	@Test
	void testNow() {
		Period period = Period.now(); 
		assertThat(period.getYear()).isEqualTo(today.getYear());
		assertThat(period.getMonth()).isEqualTo(today.getMonth());
	}

	@Test
	void testOfLocalDate() {
		Period period = Period.of(today);
		assertThat(period.getYear()).isEqualTo(today.getYear());
		assertThat(period.getMonth()).isEqualTo(today.getMonth());
	}

	@Test
	void testOf() {
		Period period = Period.of(2019, Month.DECEMBER);
		assertThat(period.getYear()).isEqualTo(2019);
		assertThat(period.getMonth()).isEqualTo(Month.DECEMBER);
	}

	@Test
	void testNext() {
		Period p1 = Period.of(2020, Month.FEBRUARY).next();
		Period p2 = Period.of(2020, Month.DECEMBER).next();
		
		assertThat(p1.getYear()).isEqualTo(2020);
		assertThat(p1.getMonth()).isEqualTo(Month.MARCH);
		
		assertThat(p2.getYear()).isEqualTo(2021);
		assertThat(p2.getMonth()).isEqualTo(Month.JANUARY);
	}

	@Test
	void testPrevious() {
		Period p1 = Period.of(2020, Month.FEBRUARY).previous();
		Period p2 = Period.of(2020, Month.JANUARY).previous();
		
		assertThat(p1.getYear()).isEqualTo(2020);
		assertThat(p1.getMonth()).isEqualTo(Month.JANUARY);
		
		assertThat(p2.getYear()).isEqualTo(2019);
		assertThat(p2.getMonth()).isEqualTo(Month.DECEMBER);
	}

	@Test
	void testMinusMonths() {
		Period p1 = Period.of(2020, Month.FEBRUARY).minusMonths(2);
		Period p2 = Period.of(2020, Month.DECEMBER).minusMonths(5);
		
		assertThat(p1.getYear()).isEqualTo(2019);
		assertThat(p1.getMonth()).isEqualTo(Month.DECEMBER);
		
		assertThat(p2.getYear()).isEqualTo(2020);
		assertThat(p2.getMonth()).isEqualTo(Month.JULY);
	}

	@Test
	void testPlusMonths() {
		Period p1 = Period.of(2020, Month.NOVEMBER).plusMonths(2);
		Period p2 = Period.of(2020, Month.FEBRUARY).plusMonths(5);
		
		assertThat(p1.getYear()).isEqualTo(2021);
		assertThat(p1.getMonth()).isEqualTo(Month.JANUARY);
		
		assertThat(p2.getYear()).isEqualTo(2020);
		assertThat(p2.getMonth()).isEqualTo(Month.JULY);
	}

	@Test
	void testIsBefore() {
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.DECEMBER);
		Period p3 = Period.of(2019, Month.FEBRUARY);
		Period p4 = Period.of(2020, Month.JANUARY);
		
		assertTrue(p1.isBefore(p2));
		assertTrue(p3.isBefore(p2));
		assertFalse(p4.isBefore(p1));
	}

	@Test
	void testIsAfter() {
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.DECEMBER);
		Period p3 = Period.of(2019, Month.FEBRUARY);
		Period p4 = Period.of(2020, Month.JANUARY);
		
		assertTrue(p2.isAfter(p1));
		assertTrue(p2.isAfter(p3));
		assertFalse(p4.isAfter(p1));
	}

	@Test
	void testDifferenceInMonths() {
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.DECEMBER);
		Period p3 = Period.of(2019, Month.FEBRUARY);
		Period p4 = Period.of(2020, Month.JANUARY);
		
		assertThat(Period.differenceInMonths(p1, p2)).isEqualTo(11);
		assertThat(Period.differenceInMonths(p3, p4)).isEqualTo(11);
		assertThat(Period.differenceInMonths(p1, p4)).isEqualTo(0);
	}

	@Test
	void testName() {
		Period p1 = Period.of(2020, Month.JANUARY);
		assertThat(p1.name()).isEqualTo(p1.getMonth()+ " "+ p1.getYear());
	}

	@Test
	void testOrdinal() {
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2019, Month.NOVEMBER);
		
		assertThat(p1.ordinal()).isEqualTo(2001);
		assertThat(p2.ordinal()).isEqualTo(1911);
	}

	@Test
	void testToString() {
		Period p1 = Period.of(2020, Month.JANUARY);
		assertThat(p1.toString()).isEqualTo(p1.getMonth()+ " "+ p1.getYear());
	}

	@Test
	void testCompareTo() {
		Period p1 = Period.of(2020, Month.JANUARY);
		Period p2 = Period.of(2020, Month.DECEMBER);
		Period p3 = Period.of(2019, Month.FEBRUARY);
		Period p4 = Period.of(2020, Month.JANUARY);
		
		assertThat(p1.compareTo(p2)).isEqualTo(-1);
		assertThat(p2.compareTo(p3)).isEqualTo(1);
		assertThat(p2.compareTo(p3)).isEqualTo(1);
		assertThat(p1.compareTo(p4)).isEqualTo(0);
	}
}
