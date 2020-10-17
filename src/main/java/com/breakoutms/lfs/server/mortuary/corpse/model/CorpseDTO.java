package com.breakoutms.lfs.server.mortuary.corpse.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.District;
import com.breakoutms.lfs.common.enums.Gender;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "corpses")
public class CorpseDTO extends RepresentationModel<CorpseDTO>{

	private String tagNo;
	
	@Size(min = 2, max = 60)
	@Nullable
	private String names;
	
	@Size(min = 2, max = 50)
	@Nullable
	private String surname;
	
	private Gender gender;
	
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	private String phycialAddress;
	
	private District district;
	
	@Column(length = 150)
	private String country;
	
	private String chief;
	
	private List<NextOfKin> nextOfKins;
	
	private LocalDate dateOfDeath;
	
	@NotNull
	private LocalDateTime arrivalDate;
	
	private String causeOfDeath;
	
	private String placeOfDeath;
	
	private String fridgeNumber;
	
	private String shelfNumber;
	
	private String receivedBy;
	
	private String driversName;
	
	private VehicleOwner vehicleOwner;
	
	private String registrationNumber;
	
	private String specialRequirements;
	
	private String otherNotes;
	
	private Integer releasedCorpseId;
	
	private String fromOtherMortuary;
}
