package com.breakoutms.lfs.server.mortuary.postmortem.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.enums.RequestPerson;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostmortemDTO {
	
	private Integer id;
	@NotNull
	private String tagNo;
	private RequestPerson requestedBy;
	private String requestPerson;
	private String phoneNumber;
	@NotNull
	private String location;

	private LocalDateTime date;

	private String driversName;
	
	private VehicleOwner vehicleOwner;
	
	private String registrationNumber;
	
	private LocalDateTime returnedTime; 

	private VehicleOwner returnTransportOwner;
	
	private String returnTransportRegNumber;
	
	private String returnTransportDriver;
	@NotNull
	private Integer requestId;
}
