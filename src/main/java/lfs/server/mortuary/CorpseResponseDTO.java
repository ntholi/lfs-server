package lfs.server.mortuary;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lfs.server.branch.District;
import lfs.server.mortuary.Corpse.Gender;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "corpseList")
public class CorpseResponseDTO extends RepresentationModel<CorpseResponseDTO>{
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
	private String specialRequirements;
	private String otherNotes;
	private boolean released;
	private LocalDateTime createdAt;
	
}
