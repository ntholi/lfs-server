package lfs.server.branch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import lfs.server.config.ConfigFile;
import lfs.server.exceptions.ConfigFileException;

@Component
public class CurrentBranch {

	private CurrentBranch() {}
	
	@Autowired
	private BranchRepository repo;
	private Branch branch;
	
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
