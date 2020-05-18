package com.breakoutms.lfs.server.common.motherbeans;

import static org.jeasy.random.FieldPredicates.named;

import org.jeasy.random.EasyRandomParameters;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.branch.Branch;

public class AuditableMother<T extends AuditableEntity<ID>, ID> extends ObjectMother<T>{
	
	@Override
	protected EasyRandomParameters getRandomParameters() {
		return super.getRandomParameters()
				.excludeField(named("branch")
				.or(named("createdAt"))
				.or(named("createdBy")))
				.randomize(named("branch"), () -> getBranch());
	}
	
	private Branch getBranch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return branch;
	}
}
