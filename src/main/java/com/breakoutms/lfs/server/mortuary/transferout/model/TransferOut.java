package com.breakoutms.lfs.server.mortuary.transferout.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.TransferStatus;
import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.transport.Transport;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;
import com.sun.istack.NotNull;

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
        name = "transfer_out_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
@SQLDelete(sql = "UPDATE sales SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
public class TransferOut extends AuditableEntity<Integer> {

	@Id
	@GeneratedValue(generator = "transfer_out_id")
	private Integer id;
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private Corpse corpse;
	
	@NotNull
	@Column(length = 50)
	private LocalDateTime date;
	
	private TransferStatus status;
	
	@ManyToOne(fetch = FetchType.LAZY)
	private Transport transport;
	
	@Column(length = 50)
	private String assistedBy;
	
	@NotNull
	@OneToOne(fetch = FetchType.LAZY, optional = false)
	private TransferRequest transferRequest;
}
