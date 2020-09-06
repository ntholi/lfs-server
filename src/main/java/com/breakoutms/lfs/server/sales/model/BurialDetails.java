package com.breakoutms.lfs.server.sales.model;

import java.time.LocalDateTime;

import javax.annotation.Nullable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.persistence.IdGenerator;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "burial_details_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE burial_details SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class BurialDetails extends AuditableEntity<Integer> {
	
	@Id
	@GeneratedValue(generator = "burial_details_id")
	private Integer id;
	
	
	//TODO: CHANGE THIS TO SALES
	@OneToOne(fetch = FetchType.LAZY)
	private Corpse corpse;
	
	private LocalDateTime leavingTime;
	
	private LocalDateTime serviceTime;
	
	@Nullable
	@Column(length = 150)
	@Size(min = 2, max = 150)
	private String burialPlace;
	
	@Nullable
	@Column(length = 150)
	@Size(min = 2, max = 150)
	private String roadStatus;
	
	@Nullable
	@Column(length = 150)
	@Size(min = 2, max = 150)
	private String physicalAddress;
}
