package com.breakoutms.lfs.server.exceptions;

import java.util.List;
import java.util.StringJoiner;

import com.breakoutms.lfs.common.enums.PolicyPaymentType;
import com.breakoutms.lfs.server.preneed.payment.model.Period;

public class PaymentAlreadyMadeException extends RuntimeException {

	private static final long serialVersionUID = 2172174832384420874L;

	public PaymentAlreadyMadeException(List<Period> period) {
		super(getMessage(period));
	}

	protected static String getMessage(List<Period> periods) {
		StringJoiner joiner = new StringJoiner(",");
		joiner.add(PolicyPaymentType.PREMIUM+" already paid for period: ");
		for(Period period: periods) {
			joiner.add(period.name());
		}
		
		return joiner.toString().trim();
	}
}
