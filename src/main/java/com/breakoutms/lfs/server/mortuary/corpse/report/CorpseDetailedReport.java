package com.breakoutms.lfs.server.mortuary.corpse.report;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.breakoutms.lfs.common.enums.District;
import com.breakoutms.lfs.common.enums.Gender;
import com.breakoutms.lfs.common.enums.VehicleOwner;

import lombok.Data;

@Data
public class CorpseDetailedReport {

	private String tagNo;
	private String names;
	private String surname;
	private Gender gender;
	private String phycialAddress;
	private District district;
	private String country;
	private LocalDate dateOfDeath;
	private LocalDateTime arrivalDate;
	private String causeOfDeath;
	
	private String fridgeNumber;
	private String shelfNumber;
	private String receivedBy;
	private String driversName;
	private VehicleOwner vehicleOwner;
	private String registrationNumber;
	private boolean released;
	
	private LocalDateTime releaseDate;
	private String dressedBy;
	private String coffinedBy;
	
	private List<NextOfKinReport> nextOfKins;
	private List<CorpseSalesProduct> salesProducts;
}
