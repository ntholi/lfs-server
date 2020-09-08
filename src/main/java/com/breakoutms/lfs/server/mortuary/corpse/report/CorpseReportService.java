package com.breakoutms.lfs.server.mortuary.corpse.report;

import static com.querydsl.core.group.GroupBy.groupBy;
import static com.querydsl.core.group.GroupBy.set;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.mortuary.corpse.model.QCorpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.QNextOfKin;
import com.breakoutms.lfs.server.mortuary.released.model.QReleasedCorpse;
import com.breakoutms.lfs.server.preneed.deceased.model.QDeceasedClient;
import com.breakoutms.lfs.server.preneed.policy.model.QPolicy;
import com.breakoutms.lfs.server.preneed.pricing.model.QFuneralScheme;
import com.breakoutms.lfs.server.products.model.QProduct;
import com.breakoutms.lfs.server.reports.AuditableRecordUtils;
import com.breakoutms.lfs.server.reports.Report;
import com.breakoutms.lfs.server.revenue.model.QRevenue;
import com.breakoutms.lfs.server.sales.model.QQuotation;
import com.breakoutms.lfs.server.sales.model.QSalesProduct;
import com.breakoutms.lfs.server.transport.QTransport;
import com.breakoutms.lfs.server.transport.QVehicle;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CorpseReportService {

	private final EntityManager entityManager;
	
	public Map<String, Object> getDetailedCorpseReport(LocalDate from, LocalDate to, Integer branch, Integer user, String tagNo) {
		QCorpse corpse = QCorpse.corpse;
		QReleasedCorpse releasedCorpse = QReleasedCorpse.releasedCorpse;
		QNextOfKin nextOfKin = QNextOfKin.nextOfKin;
		QQuotation quotation = QQuotation.quotation;
		QTransport transport = QTransport.transport;
		QVehicle vehicle = QVehicle.vehicle;
		QSalesProduct salesProduct = QSalesProduct.salesProduct;
		QProduct product = QProduct.product;
		QPolicy policy = QPolicy.policy;
		QDeceasedClient deceasedClient = QDeceasedClient.deceasedClient;
		QFuneralScheme funeralScheme = QFuneralScheme.funeralScheme;
		QRevenue revenue = QRevenue.revenue;
		
		var query = new JPAQuery<CorpseDetailedReport>(entityManager)
				.from(corpse)
				.where(corpse.tagNo.equalsIgnoreCase(tagNo))
				.leftJoin(corpse.releasedCorpse, releasedCorpse)
				.leftJoin(corpse.transport, transport)
				.leftJoin(transport.vehicle, vehicle)
				.leftJoin(corpse.nextOfKins, nextOfKin)
				.leftJoin(corpse.quotation, quotation)
				.leftJoin(quotation.salesProducts, salesProduct)
				.leftJoin(salesProduct.product, product)
				.leftJoin(quotation.revenues, revenue)
				.leftJoin(corpse.policy, policy)
				.leftJoin(policy.deceasedClients, deceasedClient)
				.leftJoin(policy.funeralScheme, funeralScheme)
				.transform(groupBy(corpse.tagNo)
					.as(Projections.bean(CorpseDetailedReport.class,
							corpse.tagNo, corpse.names, corpse.surname,
							corpse.gender, corpse.phycialAddress, corpse.district,
							corpse.country, corpse.dateOfDeath, corpse.arrivalDate,
							corpse.fridgeNumber, corpse.shelfNumber, corpse.receivedBy,
							transport.driver.as("driversName"), 
							vehicle.owner.as("vehicleOwner"),
							vehicle.registrationNumber.as("registrationNumber"),
							releasedCorpse.date.as("releaseDate"), 
							releasedCorpse.dressedBy.as("dressedBy"), releasedCorpse.coffinedBy.as("coffinedBy"),
							funeralScheme.name.as("planType"), policy.coverAmount, policy.premiumAmount.as("premium"),
							deceasedClient.payout,
							set(Projections.bean(NextOfKinReport.class, nextOfKin.id, corpse.tagNo, nextOfKin.names,
									nextOfKin.surname, nextOfKin.relationship, nextOfKin.phoneNumber)).as("nextOfKins"),
							set(Projections.bean(CorpseSalesProduct.class, salesProduct.id,
									corpse.tagNo, product.name, product.productType, 
									salesProduct.quantity, salesProduct.cost)).as("salesProducts")
						))
					//TODO: ADD AMOUNT PAID AND REMAINING BALANCE
					);
		
		return new Report<>(query.values()).getContent();
	}
	
	public Map<String, Object> getCorpseReport(LocalDate from, LocalDate to, Integer branch, Integer user) {
		QCorpse table = QCorpse.corpse;
		var query = new JPAQuery<CorpseReport>(entityManager)
				.from(table)
				.select(Projections.bean(CorpseReport.class, table.tagNo, 
						table.surname, table.names, table.arrivalDate, 
						table.dateOfDeath, table.causeOfDeath,
						table.shelfNumber, table.fridgeNumber));
		
		query = AuditableRecordUtils.filter(table._super, from, to, branch, user, query);	
		return new Report<>(query.fetch()).getContent();
	}
}
