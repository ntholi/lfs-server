package com.breakoutms.lfs.server.reception.embalming.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "embalmingRequests")
public class EmbalmingRequestDTO extends UndertakerRequestDTO<EmbalmingRequestDTO>{

	private String authorizedBy;
}
