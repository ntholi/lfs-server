package com.breakoutms.lfs.server.common.motherbeans;

import static org.jeasy.random.FieldPredicates.named;

import java.lang.reflect.Field;

import javax.persistence.Id;

import org.apache.commons.lang3.reflect.FieldUtils;
import org.jeasy.random.EasyRandomParameters;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.branch.Branch;

public class AuditableMother<T extends AuditableEntity<ID>, ID> extends ObjectMother<T>{
	
	
	public AuditableMother() {
		super();
		entity.setBranch(getBranch());
	}

	public AuditableMother<T, ID> id(ID id) {
		setIDValue(id);
		return this;
	}

	public AuditableMother<T, ID> removeIDs() {
		setIDValue(null);
		return this;
	}
	
	public AuditableMother<T, ID> removeBranch() {
		entity.setBranch(null);
		return this;
	}
	
	private void setIDValue(ID id) {
		Field[] idFields = FieldUtils.getFieldsWithAnnotation(entity.getClass(), Id.class);
		for (Field field: idFields) {
			try {
				field.setAccessible(true);
				field.set(entity, id);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	protected EasyRandomParameters getRandomParameters() {
		return super.getRandomParameters()
				.excludeField(named("branch")
				.or(named("createdAt"))
				.or(named("createdBy")));
	}
	
	private Branch getBranch() {
		Branch branch = new Branch();
		branch.setId(1);
		branch.setName("Maseru");
		branch.setSyncNumber((short)256);
		return branch;
	}
}
