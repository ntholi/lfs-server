package com.breakoutms.lfs.server.preneed.deceased;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
		setAssociations(entity);
		return repo.save(entity);
	}

	@Transactional
	public DeceasedClient update(Long id, DeceasedClient updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Deceased Client").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Deceased Client", id));
		
		setAssociations(updatedEntity);
		DeceasedClientMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}
	
	private void setAssociations(DeceasedClient entity) {
		if(StringUtils.isBlank(entity.getDependent().getId())) {
			entity.setDependent(null);
		}
		if(StringUtils.isBlank(entity.getPolicy().getPolicyNumber())) {
			entity.setPolicy(null);
		}
		String tagNo = entity.getCorpse().getTagNo();
		var corpse = corpseRepo.findById(tagNo)
				.orElseThrow(ExceptionSupplier.corpseNoteFound(tagNo));
		corpse.setPolicy(entity.getPolicy());
		
		var policyNo = entity.getPolicy().getId();
		var policy = policyRepo.findById(policyNo)
				.orElseThrow(ExceptionSupplier.policyNotFound(policyNo));
		policy.addDeceasedClient(entity);
				
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
		
		//TODO: ADD MESSAGES
		
		List<PolicyPaymentDetails> owedPayments = policyPaymentService
				.getOwedPayments(policy, Period.now())
				.stream()
				.filter(it -> it.getType() == PolicyPaymentType.PREMIUM)
				.collect(Collectors.toList());
		int unpaidMonths = owedPayments.size();
		BigDecimal deductable = scheme.getPenaltyDeductableByMonths(unpaidMonths);
		payoutAmount = payoutAmount.subtract(deductable);
		
		return new Payout(payoutAmount, null);
	}
	
	public Page<DeceasedClient> search(Specification<DeceasedClient> specs, Pageable pageable) {
        return repo.findAll(Specification.where(specs), pageable);
    }
	
	public void delete(Long id) {
		repo.deleteById(id);
	}
}
