package com.breakoutms.lfs.server.reception.embalming;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.mortuary.corpse.model.QCorpse;
import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingCertificate;
import com.breakoutms.lfs.server.reception.embalming.model.QEmbalmingRequest;
import com.breakoutms.lfs.server.reports.Report;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmbalmingCertificateService {

	private final EntityManager entityManager;
	
	public Map<String, Object> create(String tagNo){
		QEmbalmingRequest table = QEmbalmingRequest.embalmingRequest;
		QCorpse corpse = QCorpse.corpse;
		
		var res = new JPAQuery<>(entityManager)
				.from(table)
				.innerJoin(table.corpse, corpse)
				.where(corpse.tagNo.eq(tagNo))
				.select(Projections.bean(EmbalmingCertificate.class, 
						table.hair, table.beard, table.eyes, table.teeth,
						table.cosmetics, table.otherDescriptions,
						table.evidenceOfDisease, table.evidenceOfSurgery, table.dropsical,
						table.tissueGas, table.externalWounds, table.eruptions,
						table.ulcerations, table.purge, table.mutilations,
						table.rigorMortis, table.autopsyType, table.authorizedBy))
				.fetch();
		
		return new Report<>(res).getContent();
	}
}
