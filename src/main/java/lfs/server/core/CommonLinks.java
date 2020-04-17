package lfs.server.core;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.util.ArrayList;
import java.util.List;

import org.springframework.hateoas.Link;

import lfs.server.branch.Branch;
import lfs.server.branch.BranchController;

public final class CommonLinks {

	private CommonLinks() {}
	
	public static List<Link> addLinks(Class<?> controllerClass, Object id) {
		List<Link> links = new ArrayList<>();
		links.add(linkTo(controllerClass).slash(id).withSelfRel());
		links.add(linkTo(controllerClass).withRel("all"));
		return links;
	}
	
	public static List<Link> addLinksWithBranch(Class<?> controllerClass, Object id, Branch branch) {
		List<Link> list = addLinks(controllerClass, id);
		list.add(branch(branch));
		return list;
	}
	
	public static Link branch(Branch branch) {
		Integer branchId = branch != null? branch.getId() : null;
		return linkTo(BranchController.class).slash(branchId).withRel("branch");
	}
	
}
