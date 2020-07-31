package com.breakoutms.lfs.server.undertaker.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.branch.Branch;

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
public class TransferRequest extends UndertakerRequest {

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
