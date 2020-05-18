package com.breakoutms.lfs.server.preneed;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.model.PolicyViewModel;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsViewModel;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefitViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefitViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductibleViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumViewModel;

@Mapper(componentModel="spring")
public abstract class PreneedMapper {

	public static final PreneedMapper INSTANCE = Mappers.getMapper(PreneedMapper.class);
	
	public abstract FuneralScheme map(FuneralSchemeDTO funeralScheme);
	public abstract FuneralSchemeViewModel map(FuneralScheme funeralScheme);
	public abstract PremiumViewModel map(Premium premium);
	public abstract PenaltyDeductibleViewModel map(PenaltyDeductible penaltyDeductible);
	public abstract FuneralSchemeBenefitViewModel map(FuneralSchemeBenefit funeralSchemeBenefit);
	public abstract DependentBenefitViewModel map(DependentBenefit dependentBenefit);
	
	public abstract PolicyViewModel map(Policy policy);
	@Mapping(target = "funeralScheme", ignore = true)
	protected abstract Policy map(PolicyDTO dto);
	@Mapping(source = "funeralScheme.name", target = "funeralScheme")
	public abstract PolicyDTO policyToDTO(Policy policy);
	
	public abstract PolicyPayment map(PolicyPaymentDTO dto);
	public abstract PolicyPaymentViewModel map(PolicyPayment entity);
	public abstract PolicyPaymentDetailsViewModel map(PolicyPaymentDetails entity);
}
