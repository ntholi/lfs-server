package com.breakoutms.lfs.server.undertaker.transfer.model;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.RequestType;
import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;

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
@SQLDelete(sql = "UPDATE undertaker_request SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
@DiscriminatorValue("Transfer")
public class TransferRequest extends UndertakerRequest {
	
	@NotNull
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="transfer_to_branch_id")
	private Branch transferTo;

	@Override
	public String toString() {
		return "Transfer to "+ transferTo;
	}
	
	@Override
	public RequestType getRequestType() {
		return RequestType.Transfer;
	}
}
