package com.breakoutms.lfs.server.mortuary.transferout.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferOutDTO {
	
	private Integer id;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String driversName;
	private VehicleOwner vehicleOwner;
	private String registrationNumber;
	private String assistedBy;
	private Integer transferRequestId;
}
