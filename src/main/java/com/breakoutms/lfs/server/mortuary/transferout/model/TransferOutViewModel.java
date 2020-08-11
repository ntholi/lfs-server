package com.breakoutms.lfs.server.mortuary.transferout.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.TransferStatus;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "transferOut")
public class TransferOutViewModel extends RepresentationModel<TransferOutViewModel>{

	private Integer id;
	private String tagNo;
	private LocalDateTime date;
	private TransferStatus status;
	private String driversName;
	private VehicleOwner vehicleOwner;
	private String registrationNumber;
	private String assistedBy;
	private Integer requestId;
}
