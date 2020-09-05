package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.time.LocalDate;
import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.mortuary.corpse.model.QCorpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.QNextOfKin;
import com.breakoutms.lfs.server.mortuary.released.model.QReleasedCorpse;
import com.breakoutms.lfs.server.reports.AuditableRecordUtils;
import com.breakoutms.lfs.server.reports.Report;
import com.breakoutms.lfs.server.sales.model.QBurialDetails;
import com.breakoutms.lfs.server.sales.model.QQuotation;
import com.breakoutms.lfs.server.sales.model.QSales;
import com.breakoutms.lfs.server.transport.QTransport;
import com.breakoutms.lfs.server.transport.QVehicle;
import static com.querydsl.core.group.GroupBy.*;
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
		QSales sales = QSales.sales;
		QQuotation quotation = QQuotation.quotation;
		QBurialDetails burialDetails = QBurialDetails.burialDetails;
		QTransport transport = QTransport.transport;
		QVehicle vehicle = QVehicle.vehicle;
		
		var query = new JPAQuery<CorpseReport>(entityManager)
				.from(corpse)
				.leftJoin(corpse.releasedCorpse, releasedCorpse)
				.leftJoin(corpse.transport, transport)
				.leftJoin(transport.vehicle, vehicle)
				.leftJoin(corpse.nextOfKins, nextOfKin)
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
						list(Projections.bean(NextOfKinReport.class, nextOfKin.names)).as("nextOfKins")
						
				)));
		
		
		
		
		
		return new Report<>(query.values()).getContent();
//		return null;
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
