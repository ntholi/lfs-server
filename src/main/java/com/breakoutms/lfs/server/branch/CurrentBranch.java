package com.breakoutms.lfs.server.branch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.breakoutms.lfs.server.config.ConfigFile;
import com.breakoutms.lfs.server.exceptions.ConfigFileException;

@Component
public class CurrentBranch {

	//TODO: TEST HOW THIS CLASS WORKS, MAYBE IF YOU NEED Initialization-on-demand holder idiom MAYBE NOT?
	private CurrentBranch() {}
	
	@Autowired
	private BranchRepository repo;
	private Branch branch;
	
	//TODO: CONSIDERE CONCURRENCY CONTROL OVERE HERE
	public Branch get() {
		String name = null;
		if(branch == null) {
			name = ConfigFile.getBranchName();
			if(name != null) {
				branch = repo.findByName(name).orElse(null);
			}
		}
		if(branch == null) {
			throw new ConfigFileException("Unable to load branch with name: '"+ 
					name+"'");
		}
		return branch;
	}
	
	public String name() {
		branch = get();
		if(branch != null) {
			return branch.getName();
		}
		return null;
	}
}
