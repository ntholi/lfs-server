package com.breakoutms.lfs.server.preneed.payment;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.exceptions.AccountNotActiveException;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.exceptions.InvalidOperationException;
import com.breakoutms.lfs.server.exceptions.PaymentAlreadyMadeException;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetails.Type;
import com.breakoutms.lfs.server.preneed.payment.model.UnpaidPolicyPayment;
import com.breakoutms.lfs.server.preneed.policy.PolicyRepository;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyStatus;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyPaymentService {

	private final PolicyPaymentRepository repo;
	private final PolicyRepository policyRepo;
	private final UnpaidPolicyPaymentRepository owedRepo;
	
	public Optional<PolicyPayment> get(Long id) {
		return repo.findById(id);
	}
	
	public Page<PolicyPayment> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	protected List<PolicyPaymentDetails> getOwedPayments(Period currentPeriod, String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(() -> new InvalidOperationException(
						"Unable to determine owed premiums for unknown policy number '"
						+ policyNumber+"'"));
		
		List<PolicyPaymentDetails> list = new ArrayList<>();

		for (UnpaidPolicyPayment item : owedRepo.findByPolicy(policy)) {
			list.add(item.getPolicyPaymentDetails());
		}
		
		Period lastPaid = getLastPayedPeriod(policy);
		list.addAll(getOwedPremiums(policy, currentPeriod, lastPaid));
		
		BigDecimal penaltyDue = calculatePenaltyDue(policy, lastPaid);
		if(penaltyDue != null && penaltyDue.signum() > 0) {
			PolicyPaymentDetails item = PolicyPaymentDetails.penaltyOf(penaltyDue);
			item.setPolicy(policy);
			list.add(item);
		}
		
		return list;
	}
	
	@Transactional
	public PolicyPayment save(final PolicyPayment entity, String policyNumber) {
		Policy policy = policyRepo.findById(policyNumber)
				.orElseThrow(ExceptionSupplier.policyNotFound(policyNumber));
		
		if(policy.getStatus() == PolicyStatus.DEACTIVATED) {
			//TODO: Add logic for re-activating deactivated policy
			Period period = getLastPayedPeriod(policy);
			String msg = "Account has been deactivated, "
					+ "last premium payment was for period "+period;
			throw new AccountNotActiveException(msg);
		}
		
//		TODO: getOwedPayments has to be cached for each policyNumber catch value deleted here
		List<UnpaidPolicyPayment> notPaid = getOwedPayments(Period.now(), policyNumber)
				.stream()
				.filter(it -> !entity.getPolicyPaymentDetails().contains(it))
				.map(UnpaidPolicyPayment::new)
				.collect(Collectors.toList());
		if(!notPaid.isEmpty()) {
			owedRepo.saveAll(notPaid);
		}
		
		entity.setPolicy(policy);
		entity.getPolicyPaymentDetails().forEach(it -> it.setPolicy(policy));
		
		var periods = getAlreadyPaidPremiums(entity, policyNumber);
		if(!periods.isEmpty()) {
			throw new PaymentAlreadyMadeException(periods);
		}
		return repo.save(entity);
	}

	protected List<Period> getAlreadyPaidPremiums(final PolicyPayment entity, String policyNumber) {
		Set<PolicyPaymentDetails> payments = entity.getPolicyPaymentDetails();
		Set<String> premiumIds = new HashSet<>();
		for (PolicyPaymentDetails it : payments) {
			if(it.getType() == Type.PREMIUM) {
				final String premiumId = generatePremiumId(policyNumber, it);
				it.setPremiumPaymentId(premiumId);
				premiumIds.add(premiumId);
			}
		}
		return repo.findPeriodsByPaymentIds(premiumIds);
	}

	private int periodAsInt(PolicyPaymentDetails paymentDetails) {
		Period period = paymentDetails.getPeriod();
		return period.getYear() + period.getMonth().getValue();
	}

	@Transactional
	public PolicyPayment update(Long id, PolicyPayment entity) {
		if(entity == null) {
			throw ExceptionSupplier.nullUpdate("Policy Payment").get();
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

	protected List<PolicyPaymentDetails> getPaymentDetails(Long policyPaymentId) {
		return repo.getPaymentDetails(policyPaymentId);
	}

	protected String generatePremiumId(String policyNumber, PolicyPaymentDetails premium) {
		if(premium.getType() != Type.PREMIUM) {
			throw new IllegalArgumentException("PolicyPaymentDetails: '"+premium
					+" should be of type "+Type.PREMIUM);
		}
		Period period = premium.getPeriod();
		Objects.requireNonNull(period, "Period for PREMIUM cannot be null");
		
		String year = String.valueOf(period.getYear()).substring(2);
		Integer month = period.getMonth().getValue();
		return policyNumber+year+month;
	}
	
	private Period getLastPayedPeriod(Policy policy) {
		Optional<Period> periodOpt = repo.getLastPayedPeriod(policy);
		Period period = null;
		if(periodOpt.isPresent()){
			period = periodOpt.get();
		}
		else {
			LocalDate regDate = policy.getRegistrationDate();
			period = new Period();
			period.setYear(regDate.getYear());
			FuneralScheme scheme = policy.getFuneralScheme();
			if(scheme.isIncludesFirstPremium()) {
				period.setMonth(regDate.getMonth().plus(1));
			}
			else {
				period.setMonth(regDate.getMonth());
			}
		}
		return period;
	}

	private List<PolicyPaymentDetails> getOwedPremiums(Policy policy, Period currentPeriod, Period lastPaid) {
		BigDecimal stdPremium = policy.getPremiumAmount();
		
		return populateOwedPeriods(lastPaid, currentPeriod)
				.stream()
				.map(it -> new PolicyPaymentDetails(Type.PREMIUM, stdPremium, it, policy))
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
