package com.breakoutms.lfs.server.preneed.payment;

import org.springframework.stereotype.Repository;

import com.breakoutms.lfs.server.core.AuditableRepository;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;

@Repository
public interface PolicyPaymentRepository extends AuditableRepository<PolicyPayment, Long>{

}
