package com.breakoutms.lfs.server.undertaker.transfer.model;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.constraints.NotNull;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.RequestType;
import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;

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
@PrimaryKeyJoinColumn(name = "mortuary_request_id")
public class TransferRequest extends MortuaryRequest {
	
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
		return RequestType.TRANSFER;
	}
}
