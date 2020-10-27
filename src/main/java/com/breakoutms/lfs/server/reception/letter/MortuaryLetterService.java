package com.breakoutms.lfs.server.reception.letter;

import java.util.Map;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Service;

import com.breakoutms.lfs.server.mortuary.corpse.model.QCorpse;
import com.breakoutms.lfs.server.mortuary.released.model.QReleasedCorpse;
import com.breakoutms.lfs.server.reports.Report;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class MortuaryLetterService {

	private final EntityManager entityManager;
	
	public Map<String, Object> create(String tagNo){
		QCorpse corpse = QCorpse.corpse;
		QReleasedCorpse releasedCorpse = QReleasedCorpse.releasedCorpse;
		
		var res = new JPAQuery<>(entityManager)
				.from(corpse)
				.leftJoin(corpse.releasedCorpse, releasedCorpse)
				.where(corpse.tagNo.eq(tagNo))
				.select(Projections.bean(MortuaryLetter.class, 
						corpse.names.concat(" ").concat(corpse.surname).as("corpseNames"),
						corpse.arrivalDate.as("_arrivalDate"), releasedCorpse.date.as("_releaseDate")))
				.fetch();
		
		return new Report<>(res).getContent();
	}
}
