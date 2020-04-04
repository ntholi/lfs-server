package lfs.server.core;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import org.springframework.hateoas.Link;

import lfs.server.branch.Branch;
import lfs.server.branch.BranchController;

public class CommonLinks {

	private CommonLinks() {}
	
	public static Link branch(Branch branch) {
		return linkTo(BranchController.class).slash(branch.getId()).withRel("branch");
	}
}
