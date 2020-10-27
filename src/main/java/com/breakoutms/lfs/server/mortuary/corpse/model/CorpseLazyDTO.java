package com.breakoutms.lfs.server.mortuary.corpse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.Gender;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "corpses")
public class CorpseLazyDTO extends RepresentationModel<CorpseLazyDTO>{

	private String tagNo;
	
	private String names;
	
	private String surname;
	
	private Gender gender;
	
	private LocalDate dateOfBirth;
	
	private String phycialAddress;
	
	private LocalDate dateOfDeath;
	
	private LocalDateTime arrivalDate;
	
	private String causeOfDeath;
	
	private String fridgeNumber;
	
	private String shelfNumber;
	
	private String receivedBy;
	
	private Integer releasedCorpseId;
		
	public boolean getReleased() {
		return releasedCorpseId != null;
	}
}
