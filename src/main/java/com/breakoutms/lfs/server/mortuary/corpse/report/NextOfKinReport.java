package com.breakoutms.lfs.server.mortuary.corpse.report;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

@Data
public class NextOfKinReport {
	
	@JsonIgnore 
	private Integer id;
	private String tagNo;
    private String names;
	private String surname;
    private String relationship;
    private String phoneNumber;
}
