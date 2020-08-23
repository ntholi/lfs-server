package com.breakoutms.lfs.server.preneed.deceased.model;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class Payout extends RepresentationModel<Payout> {
	
	private BigDecimal payout;
	
	private List<String> messages;

}
