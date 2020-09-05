package com.breakoutms.lfs.server.mortuary.corpse.report;

import lombok.Data;

@Data
public class NextOfKinReport {
	
	private String tagNo;
    private String names;
	private String surname;
    private String relationship;
    private String phoneNumber;
}
