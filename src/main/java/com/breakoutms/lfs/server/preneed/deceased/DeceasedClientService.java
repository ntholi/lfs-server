package com.breakoutms.lfs.server.preneed.deceased;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.common.enums.PolicyPaymentType;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseRepository;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.preneed.deceased.model.Payout;
import com.breakoutms.lfs.server.preneed.payment.PolicyPaymentService;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class DeceasedClientService {


	private final DeceasedClientRepository repo;
	private final PolicyRepository policyRepo;
	private final PolicyPaymentService policyPaymentService;
	private final CorpseRepository corpseRepo;
	
	public Optional<DeceasedClient> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<DeceasedClient> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public DeceasedClient save(final DeceasedClient entity, String policyNumber) {
		return repo.save(entity);
	}
	
	@Transactional(readOnly = true)
	public List<CorpseLookupProjection> lookup(String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(ExceptionSupplier.policyNotFound(policyNumber));
		String names = policy.getFullName();
		return corpseRepo.lookup(names);
	}
	
	@Transactional(readOnly = true)
	public Payout getPayout(String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(ExceptionSupplier.policyNotFound(policyNumber));
		FuneralScheme scheme = policy.getFuneralScheme();
		BigDecimal payoutAmount = policy.getCoverAmount();
		
		Period lastPaid = policyPaymentService.getLastPayedPeriod(policy);
		if(lastPaid.plusMonths(scheme.getMonthsBeforeDeactivated()).isBefore(Period.now())) {
			Payout payout = new Payout(new BigDecimal(0), null);
			return payout;
		}
		
		List<PolicyPaymentDetails> owedPayments = policyPaymentService
				.getOwedPayments(policy, Period.now())
				.stream()
				.filter(it -> it.getType() == PolicyPaymentType.PREMIUM)
				.collect(Collectors.toList());
		int unpaidMonths = owedPayments.size();
		BigDecimal deductable = scheme.getPenaltyDeductableByMonths(unpaidMonths);
		payoutAmount = payoutAmount.min(deductable);
		
		return new Payout(payoutAmount, null);
	}
	
	@Transactional
	public DeceasedClient update(Long id, DeceasedClient updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Deceased Client").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Deceased Client", id));
		
		DeceasedClientMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}
}
