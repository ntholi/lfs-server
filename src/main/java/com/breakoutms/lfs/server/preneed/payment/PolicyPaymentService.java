package com.breakoutms.lfs.server.preneed.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.preneed.PolicyRepository;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails.Type;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyPaymentService {

	private final PolicyPaymentRepository repo;
	private final PolicyRepository policyRepo;
	
	public Optional<PolicyPayment> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<PolicyPayment> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public PolicyPayment save(final PolicyPayment entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public PolicyPayment update(Long id, PolicyPayment entity) {
		if(entity == null) {
			throw ExceptionSupplier.notFoundOnUpdate("Policy Payment").get();
		}
		if(!repo.existsById(id)) {
			throw ExceptionSupplier.notFound("Policy Payment", id).get();
		}
		entity.setId(id);
		return repo.save(entity);
	}

	public void delete(Long id) {
		repo.deleteById(id);
	}

	public List<PolicyPaymentDetails> getPaymentDetails(Long policyPaymentId) {
		return repo.getPaymentDetails(policyPaymentId);
	}

	@Transactional
	public List<PolicyPaymentDetails> getOwedPayments(Period currentPeriod, String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(() -> new InvalidOperationException(
						"Unable to determine owed premiums for unknown policy number '"
						+ policyNumber+"'"));
		
		Period lastPaid = repo.getLastPayedPeriod(policy.getPolicyNumber());
		List<PolicyPaymentDetails> list = new ArrayList<>();
		

		for (UnpaidPolicyPayment item : repo.getUnpaidPolicyPayment(policy.getPolicyNumber())) {
			list.add(item.getPolicyPaymentDetails());
		}
		
		list.addAll(getOwedPremiums(policy, currentPeriod, lastPaid));
		
		BigDecimal penaltyDue = calculatePenaltyDue(policy, lastPaid);
		if(penaltyDue != null && penaltyDue.signum() > 0) {
			list.add(PolicyPaymentDetails.penaltyOf(penaltyDue));
		}
		
		return list;
	}

	private List<PolicyPaymentDetails> getOwedPremiums(Policy policy, Period currentPeriod, Period lastPaid) {
		BigDecimal stdPremium = policy.getPremiumAmount();
		
		return populateOwedPeriods(lastPaid, currentPeriod)
				.stream()
				.map(it -> new PolicyPaymentDetails(Type.PREMIUM, stdPremium, it))
				.collect(Collectors.toList());
	}
	
	private BigDecimal calculatePenaltyDue(Policy policy, Period lastPaymentPeriod) {
		FuneralScheme funeralScheme = policy.getFuneralScheme();
		int overdueMonths = getMonthsOverdue(policy, lastPaymentPeriod);
		return funeralScheme.getPenaltyFee().multiply(new BigDecimal(overdueMonths));
	}
	
	private int getMonthsOverdue(Policy policy, Period lastPaymentPeriod) {
		int months = Period.differenceInMonths(Period.of(LocalDate.now()), lastPaymentPeriod);
		months -= 1; // minus one for the current month
		int allowed = policy.getFuneralScheme().getMonthsBeforePenalty();
		if(allowed <= 0) {
			return months;
		}
		if(months > 0) {
			return Math.floorDiv(months, allowed);
		}
		return 0;
	}
	
	private List<Period> populateOwedPeriods(Period lastPeriod, Period currentPeriod){
		List<Period> list = new ArrayList<>();
		int months = Period.differenceInMonths(currentPeriod, lastPeriod);
		for (int i = 0; i < months; i++) {
			lastPeriod = lastPeriod.plusMonths(1);
			list.add(lastPeriod);
		}
		return list;
	}
}
