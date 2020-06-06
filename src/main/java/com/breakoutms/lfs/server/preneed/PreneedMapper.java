package com.breakoutms.lfs.server.preneed;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyViewModel;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsDTO;
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
	public abstract Policy map(PolicyDTO dto);
	
	public abstract PolicyPayment map(PolicyPaymentDTO dto);
	@Mapping(source = "month", target = "period.month")
	@Mapping(source = "year", target = "period.year")
	public abstract PolicyPaymentDetails map(PolicyPaymentDetailsDTO dto);
	public abstract PolicyPaymentViewModel map(PolicyPayment entity);
	public abstract PolicyPaymentDetailsViewModel map(PolicyPaymentDetails entity);
	public abstract List<PolicyPaymentDetailsViewModel> map(List<PolicyPaymentDetails> list);
}
