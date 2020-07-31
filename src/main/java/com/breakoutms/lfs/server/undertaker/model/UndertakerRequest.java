package com.breakoutms.lfs.server.undertaker.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
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
        name = "undertaker_request_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
@SQLDelete(sql = "UPDATE undertaker_request SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
public abstract class UndertakerRequest extends AuditableEntity<Long>{
	public enum RequestType{
		Postmortem,
		Transfer;
		
		@Override
		public String toString() {
			if(ordinal() == 0) {
				return "Postmortem Request";
			}
			return "Transfer Request";
		}
	}
	
	@Id
	@GeneratedValue(generator = "undertaker_request_id")
	private Long id;

	
	@ManyToOne(fetch = FetchType.LAZY)
	private Corpse corpse;
	
	private boolean opened;

	public abstract RequestType getRequestType();
}
