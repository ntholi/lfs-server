package com.breakoutms.lfs.server.mortuary.postmortem.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "postmortems")
public class PostmortemDTO extends RepresentationModel<PostmortemDTO>{
	
	private Integer id;
	@NotNull
	private String tagNo;
	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	private String location;
	private LocalDateTime date;
	private String driversName;
	private VehicleOwner vehicleOwner;
	private String registrationNumber;
	private LocalDateTime returnedTime; 
	private String returnTransportDriver;
	private VehicleOwner returnTransportOwner;
	private String returnTransportRegNumber;
	@NotNull
	private Integer requestId;
}
