package com.breakoutms.lfs.server.mortuary.postmortem.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.transport.Transport;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;

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
        name = "postmortem_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE postmortem SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class Postmortem extends AuditableEntity<Integer> {
	
	@Id
	@GeneratedValue(generator = "postmortem_id")
	private Integer id;

	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(nullable = false)
	private Corpse corpse;
	
	private LocalDateTime date;
	
	@ManyToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL) 
	private Transport transport;
	
	private LocalDateTime returnedTime;
	
	@ManyToOne(fetch = FetchType.LAZY,
			cascade = CascadeType.ALL) 
	private Transport returnTransport;
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private PostmortemRequest postmortemRequest;
}
