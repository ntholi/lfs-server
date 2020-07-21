package com.breakoutms.lfs.server.preneed.payment;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsViewModel;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentViewModel;

@Mapper(componentModel="spring")
public abstract class PolicyPaymentMapper {
	
	public static final PolicyPaymentMapper INSTANCE = Mappers.getMapper(PolicyPaymentMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "policy", ignore = true)
	public abstract void update(PolicyPayment updatedEntity, @MappingTarget PolicyPayment saved);
	
	public abstract PolicyPayment map(PolicyPaymentDTO dto);
	
	public abstract PolicyPaymentDetails map(PolicyPaymentDetailsDTO dto);
	
	@Mapping(source = "policy.policyNumber", target = "policyNumber")
	public abstract PolicyPaymentViewModel map(PolicyPayment entity);
	
	public abstract PolicyPaymentDetailsViewModel map(PolicyPaymentDetails entity);
	public abstract List<PolicyPaymentDetailsViewModel> map(List<PolicyPaymentDetails> list);
	public abstract PolicyPaymentDTO toDTO(PolicyPayment entity);
}
