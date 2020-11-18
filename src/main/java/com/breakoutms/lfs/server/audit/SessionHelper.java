package com.breakoutms.lfs.server.audit;

import com.breakoutms.lfs.server.branch.BranchService;
import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.security.JwtUtils;
import com.breakoutms.lfs.server.util.BeanUtil;

public class SessionHelper {

	private SessionHelper() {}

	private static BranchService branchService = BeanUtil.getBean(BranchService.class);
	private static JwtUtils jwtUtils = BeanUtil.getBean(JwtUtils.class);
	
	public static Branch getBranch() {
		Integer branchId = jwtUtils.getBranchId(JwtUtils.getAccessToken());
		var branchOp = branchService.get(branchId);
		if(branchOp.isPresent()) {
			return branchOp.get();
		}
		return null;
	}
}
