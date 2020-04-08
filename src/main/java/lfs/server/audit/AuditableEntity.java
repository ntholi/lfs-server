/**
 * 
 */
package lfs.server.audit;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lfs.server.branch.Branch;
import lfs.server.branch.CurrentBranch;
import lfs.server.util.BeanUtil;
import lombok.Data;

/**
 * @author Ntholi Nkhatho
 *
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity {
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable=true, updatable=false)
	private Branch branch;
	
	@CreatedDate
	@Column(nullable=true, updatable=false)
	private LocalDateTime createdAt;
	
	private boolean deleted;
	
	@PrePersist
	void prePersist() {
		if(branch == null) {
			//TODO: consider an efficient way of doing this, or maybe this is the best you can implement it, I don't know
			branch = BeanUtil.getBean(CurrentBranch.class).get();
		}
	}
}
