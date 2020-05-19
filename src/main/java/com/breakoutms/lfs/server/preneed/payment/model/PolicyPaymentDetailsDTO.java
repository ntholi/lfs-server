package com.breakoutms.lfs.server.preneed.payment.model;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.server.audit.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @Builder
@AllArgsConstructor @NoArgsConstructor
public class PolicyPaymentDetailsDTO {

	public enum Type{
		PREMIUM, PENALTY, REGISTRATION, UPGRADE_FEE
	}
	
	@NotNull
	private Type type;
	
	@NotNull
	private Period period;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	private BigDecimal amount;
	
	private boolean markedAsPaid;
}
