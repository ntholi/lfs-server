package com.breakoutms.lfs.server.mortuary.transferout.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "transferOut")
public class TransferOutDTO extends RepresentationModel<TransferOutDTO>{
	
	private Integer id;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String driversName;
	private VehicleOwner vehicleOwner;
	private String registrationNumber;
	private String assistedBy;
	@NotNull
	private Integer requestId;
}
