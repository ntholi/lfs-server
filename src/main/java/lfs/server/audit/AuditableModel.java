/**
 * 
 */
package lfs.server.audit;

import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;

import lfs.server.branch.Branch;
import lfs.server.branch.CurrentBranch;
import lombok.Data;

/**
 * @author Ntholi Nkhatho
 *
 */
@MappedSuperclass
@Data
public abstract class AuditableModel {
	
	@ManyToOne 
	@JoinColumn(name="branch_id", 
		nullable=true, updatable=false)
	private Branch branch;
	
	@PrePersist
	void prePersist() {
		if(branch == null) {
			branch = new CurrentBranch().get();
		}
	}
}
