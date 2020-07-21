package com.breakoutms.lfs.server.common;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsDTO;

@Mapper(componentModel="spring")
public abstract class MappersForTests {

	public static final MappersForTests INSTANCE = Mappers.getMapper(MappersForTests.class);
	
	@Mapping(source = "funeralScheme.name", target = "funeralScheme")
	public abstract PolicyDTO map(Policy policy);

	public abstract PolicyPaymentDTO map(PolicyPayment policyPayment);
	
	public abstract PolicyPaymentDetailsDTO map(PolicyPaymentDetails policyPaymentDetails);

}
