package com.breakoutms.lfs.server.mortuary.corpse;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.persistence.EntityManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.breakoutms.lfs.server.audit.QAuditableEntity;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;
import com.breakoutms.lfs.server.mortuary.corpse.model.OtherMortuary;
import com.breakoutms.lfs.server.mortuary.corpse.model.QCorpse;
import com.breakoutms.lfs.server.mortuary.corpse.report.CorpseReport;
import com.breakoutms.lfs.server.reports.Report;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQuery;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CorpseService {

	private final CorpseRepository repo;
	private final OtherMortuaryRepository otherMortuaryRepo;
	private final EntityManager entityManager;
	
	public Optional<Corpse> get(String id) {
		return repo.findById(id);
	}
	
	public Page<Corpse> all(Pageable pageable) {
		return repo.findAll(pageable);
	}
	
	@Transactional
	public Corpse save(final Corpse entity) {
		return repo.save(entity);
	}
	
	@Transactional
	public Corpse update(String id, Corpse updatedEntity) {
		if(updatedEntity == null) {
			throw ExceptionSupplier.nullUpdate("Corpse").get();
		}
		var entity = repo.findById(id)
				.orElseThrow(ExceptionSupplier.notFound("Corpse", id));
		
		if(entity.getTransferredFrom() != null) {
			OtherMortuary om = entity.getTransferredFrom();
			if(om.getId() == null && StringUtils.isNotBlank(om.getName())) {
				Optional<OtherMortuary> obj = otherMortuaryRepo.findFirstByName(om.getName());
				if(obj.isPresent()) {
					entity.setTransferredFrom(obj.get());
				}
			}
			else if(!otherMortuaryRepo.existsById(om.getId())) {
				om.setId(null);
			}
		}
		
		CorpseMapper.INSTANCE.update(updatedEntity, entity);
		return repo.save(entity);
	}

	public void delete(String id) {
		repo.deleteById(id);
	}
	
	public List<NextOfKin> getNextOfKins(String tagNo) {
		return repo.findNextOfKins(tagNo);
	}
	
	public List<OtherMortuary> getOtherMortuaries(){
		return otherMortuaryRepo.findAll();
	}

	public Optional<OtherMortuary> getTransforedFrom(int id) {
		return otherMortuaryRepo.findById(id);
	}

	public List<CorpseLookupProjection> lookup(String names) {
		return repo.lookup(names);
	}

	public Map<String, Object> getCorpseReport(LocalDate from, LocalDate to, Integer branch, Integer user) {
		QCorpse table = QCorpse.corpse;
		var query = new JPAQuery<CorpseReport>(entityManager)
				.from(table)
				.select(Projections.bean(CorpseReport.class, table.tagNo, 
						table.surname, table.names, table.arrivalDate, 
						table.dateOfDeath, table.causeOfDeath,
						table.shelfNumber, table.fridgeNumber));
		
		query = common(table._super, from, to, branch, user, query);	
		return new Report<>(query.fetch()).getContent();
	}

	private <T> JPAQuery<T> common(QAuditableEntity table, LocalDate from, LocalDate to, Integer branch, Integer user,
			JPAQuery<T> query) {
		query = query.where(table.deleted.isFalse());
		if(from != null) {
			query = query.where(table.createdAt.after(from.atStartOfDay()));
		}
		if(to != null) {
			query = query.where(table.createdAt.before(to.atTime(LocalTime.MAX)));
		}
		if(branch != null) {
			query = query.where(table.branch.id.eq(branch));
		}
		if(user != null) {
			query = query.where(table.createdBy.eq(user));
		}
		return query;
	}
}
