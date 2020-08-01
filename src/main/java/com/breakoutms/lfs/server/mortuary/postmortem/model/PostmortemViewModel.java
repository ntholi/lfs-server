package com.breakoutms.lfs.server.mortuary.postmortem.model;

import java.time.LocalDateTime;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "postmortemRequests")
public class PostmortemViewModel extends RepresentationModel<PostmortemViewModel>{

	private Integer id;
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
}
