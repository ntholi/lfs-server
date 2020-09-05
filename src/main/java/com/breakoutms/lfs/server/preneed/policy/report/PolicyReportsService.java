package com.breakoutms.lfs.server.preneed.policy.report;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.preneed.policy.model.QPolicy;
import com.breakoutms.lfs.server.preneed.pricing.model.QFuneralScheme;
import com.breakoutms.lfs.server.reports.AuditableRecordUtils;
import com.breakoutms.lfs.server.reports.Report;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.NumberTemplate;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class PolicyReportsService {

	private final EntityManager entityManager;
	
	
	public Map<String, Object> getPlanTypeSummaryReport(LocalDate from, LocalDate to, Integer branch, Integer userId){
		QPolicy table = QPolicy.policy;
		QFuneralScheme funeralScheme = QFuneralScheme.funeralScheme;
		
		var  query =  new JPAQuery<>(entityManager)
				.from(table)
				.innerJoin(table.funeralScheme, funeralScheme)
				.groupBy(funeralScheme.id)
				.select(Projections.bean(PlanTypeSummaryReport.class,
						table.dateOfBirth.year().avg().as("year"),
						funeralScheme.name.as("name"),
						table.funeralScheme.count().as("count")));
		
		var res = AuditableRecordUtils.filter(table._super, 
				from, to, branch, userId, query).fetch();	
		return new Report<>(res).getContent();
	}
	
	
	public Map<String, Object> getPolicyReport(LocalDate from, LocalDate to, Integer branch, Integer userId){
		QPolicy table = QPolicy.policy;
		QFuneralScheme funeralScheme = QFuneralScheme.funeralScheme;
		
		var  query =  new JPAQuery<>(entityManager)
				.from(table)
				.innerJoin(table.funeralScheme, funeralScheme)
				.select(Projections.bean(PolicyReport.class, table.policyNumber, 
						table.surname.concat(" ").concat(table.names).as("names"), table.gender, 
						table.registrationDate, table.dateOfBirth,
						table.premiumAmount, table.coverAmount,
						table.funeralScheme.name.as("planType")));
		
		var res = AuditableRecordUtils.filter(table._super, 
				from, to, branch, userId, query).fetch();	
		return new Report<>(res).getContent();
	}
}
