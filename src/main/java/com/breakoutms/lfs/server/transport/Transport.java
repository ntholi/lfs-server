package com.breakoutms.lfs.server.transport;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

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
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "transport_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE transport SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Transport extends AuditableEntity<Integer>{

	@Id
	@GeneratedValue(generator = "transport_id")
	private Integer id;
	
	@Length(max = 40)
	@Column(length = 40)
	private String driver;
	
	@ManyToOne(fetch = FetchType.LAZY, 
			cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Vehicle vehicle;
	
	public VehicleOwner getVehicleOwner() {
		if(vehicle != null) {
			return vehicle.getOwner();
		}
		return null;
	}
}
