package com.breakoutms.lfs.server.transport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;
import org.hibernate.validator.constraints.Length;

import com.breakoutms.lfs.common.enums.VehicleOwner;
import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.persistence.IdGenerator;

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
@Table(indexes = {
        @Index(columnList = "registrationNumber", name = "unique_registration_number", unique=true)
})
@GenericGenerator(
        name = "vehicle_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE vehicle SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Vehicle extends AuditableEntity<Integer>{

	@Id
	@GeneratedValue(generator = "vehicle_id")
	private Integer id;
	
	@Length(max = 16)
	@Column(length = 16, unique = true)
	private String registrationNumber;
	
	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('LFS','OWN','POLICE','OTHER')")
	private VehicleOwner owner;
	
	@Override
	public Integer getId() {
		return id;
	}
}
