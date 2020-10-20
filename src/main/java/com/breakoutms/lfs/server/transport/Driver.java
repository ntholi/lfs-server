package com.breakoutms.lfs.server.transport;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

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
        name = "driver_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@Table(indexes = {
        @Index(columnList = "name", name = "unique_drivers_name", unique=true)
})
@SQLDelete(sql = "UPDATE driver SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Driver  extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "driver_id")
	private Integer id;
	@Column(length = 50, nullable = false)
	private String name;
}
