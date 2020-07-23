package com.breakoutms.lfs.server.transport;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.VehicleOwner;
import com.breakoutms.lfs.server.audit.AuditableEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@SQLDelete(sql = "UPDATE corpse SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Vehicle extends AuditableEntity<String>{

	@Id
	private String registrationNumber;
	@Enumerated(EnumType.STRING)
	private VehicleOwner owner;
	
	@Override
	public String getId() {
		return registrationNumber;
	}
}
