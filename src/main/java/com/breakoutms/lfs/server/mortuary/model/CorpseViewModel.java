package com.breakoutms.lfs.server.mortuary.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.core.entity.District;
import com.breakoutms.lfs.server.core.entity.Gender;
import com.breakoutms.lfs.server.transport.Owner;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "products")
public class CorpseViewModel extends RepresentationModel<CorpseViewModel>{
	
	private String tagNo;
	
	private String names;
	
	private String surname;
	
	private Gender gender;
	
	private LocalDate dateOfBirth;
	
	private String phycialAddress;
	
	private District district;
	
	private String chief;

	private LocalDate dateOfDeath;
	
	private LocalDateTime arrivalDate;
	
	private String causeOfDeath;
	
	private String placeOfDeath;
	
	private String fridgeNumber;
	
	private String shelfNumber;
	
	private String receivedBy;
	
	private String driversName;
	
	private Owner vehicleOwner;
	
	private String registrationNumber;
	
	private String specialRequirements;
	
	private String otherNotes;
	
	private boolean released;
	
	private String transferredFrom;
}
