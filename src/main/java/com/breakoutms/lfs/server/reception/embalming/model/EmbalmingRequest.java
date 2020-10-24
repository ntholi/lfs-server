package com.breakoutms.lfs.server.reception.embalming.model;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.RequestType;
import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.persistence.IdGenerator;
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
@GenericGenerator(
        name = "embalming_request_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})

@SQLDelete(sql = "UPDATE embalming_request SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
@DiscriminatorValue(RequestType.Const.EMBALMING)
public class EmbalmingRequest extends UndertakerRequest {

	@Column(length = 15)
	private String authorizedBy;

	@Override
	public RequestType getRequestType() {
		return RequestType.EMBALMING;
	}
}
