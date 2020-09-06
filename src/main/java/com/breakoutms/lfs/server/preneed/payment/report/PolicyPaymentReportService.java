package com.breakoutms.lfs.server.preneed.payment.report;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.preneed.payment.model.QPolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.QPolicyPaymentDetails;
import com.breakoutms.lfs.server.preneed.policy.model.QPolicy;
import com.breakoutms.lfs.server.preneed.pricing.model.QFuneralScheme;
import com.breakoutms.lfs.server.reports.AuditableRecordUtils;
import com.breakoutms.lfs.server.reports.Report;
import com.breakoutms.lfs.server.revenue.model.QRevenue;
import com.breakoutms.lfs.server.revenue.report.RevenueUser;
import com.breakoutms.lfs.server.user.model.QUser;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyPaymentReportService {

	private final EntityManager entityManager;
	
	
	public Map<String, Object> getCollections(LocalDate from, LocalDate to, Integer branch, Integer userId) {
		QPolicyPayment payment = QPolicyPayment.policyPayment;
		QUser user = QUser.user;
		var  query =  new JPAQuery<>(entityManager)
				.from(payment)
				.innerJoin(payment.createdBy, user)
				.select(Projections.bean(PolicyPaymentUser.class, 
						user.firstName.concat(" ").concat(user.lastName).as("names"), 
						payment.amountTendered.subtract(payment.change).sum().as("amount")))
				.groupBy(user);
		
		var res = AuditableRecordUtils.filter(payment._super, 
				from, to, branch, userId, query).fetch();
		
		return new Report<>(res).getContent();
	}
	
	public Map<String, Object> getPolicyPaymentReport(LocalDate from, LocalDate to, Integer branch, Integer userId){
		QPolicyPaymentDetails table = QPolicyPaymentDetails.policyPaymentDetails;
		QPolicyPayment policyPayment = QPolicyPayment.policyPayment;
		QPolicy policy = QPolicy.policy;
		QFuneralScheme funeralScheme = QFuneralScheme.funeralScheme;
		
		var  query =  new JPAQuery<>(entityManager)
				.from(table)
				.innerJoin(table.policyPayment, policyPayment)
				.innerJoin(table.policy, policy)
				.innerJoin(policy.funeralScheme, funeralScheme)
				.select(Projections.bean(PolicyPaymentReport.class,policy.policyNumber, 
						policy.names.concat(" ").concat(policy.surname).as("names"),
						funeralScheme.name.as("planType"),
						table.period.as("_period"), table.amount,
						table.type.as("paymentType"),
						policyPayment.paymentDate.as("date")));
		
		var res = AuditableRecordUtils.filter(table._super, 
				from, to, branch, userId, query).fetch();	
		return new Report<>(res).getContent();
	}
}
