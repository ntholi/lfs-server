/**
 * 
 */
package com.breakoutms.lfs.server.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.breakoutms.lfs.server.branch.Branch;
import com.breakoutms.lfs.server.core.Entity;
import com.breakoutms.lfs.server.user.model.User;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author Ntholi Nkhatho
 * @param <ID> Entity's ID type
 *
 */
@Data
@EqualsAndHashCode(exclude = {"createdBy", "branch"})
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity<ID> implements Entity<ID> {
	
	public static final String CLAUSE = "deleted = false";
	
	//I've Disabled @NotNull so that validations for Product so that it can be validated
	//in the controller
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable=false)
	private Branch branch;
	
	@CreatedDate
	@Column(nullable=true, updatable=false)
	protected LocalDateTime createdAt;
	
	@CreatedBy
//	@JoinColumn(updatable=false, nullable = false)
	@ManyToOne(fetch = FetchType.LAZY)
	private User createdBy;
	
	@Column(columnDefinition = "BIT(1) default 0")
	private boolean deleted;
	
	@PrePersist
	void beforeSaving() {
		if(createdBy != null && branch == null) {
			branch = createdBy.getBranch();
		}
	}
}
