package com.breakoutms.lfs.server.client_startup;

import java.util.List;

import com.breakoutms.lfs.server.branch.Branch;

import lombok.Data;

@Data
public class StartupData {
	
	private List<String> funeralSchemes;
	private Iterable<Branch> branches;
}
