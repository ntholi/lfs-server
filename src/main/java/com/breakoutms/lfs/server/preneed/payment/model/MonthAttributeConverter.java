package com.breakoutms.lfs.server.preneed.payment.model;

import java.time.Month;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class MonthAttributeConverter implements AttributeConverter<Month, Integer> {

	@Override
	public Integer convertToDatabaseColumn(Month month) {
		return month != null? month.getValue(): null;
	}

	@Override
	public Month convertToEntityAttribute(Integer ordinal) {
		return ordinal != null? fromInt(ordinal): null;
	}

	public static Month fromInt(Integer month) {
		return Month.values()[month-1];
	}

}
