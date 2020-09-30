package com.breakoutms.lfs.server.undertaker.transfer.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.undertaker.model.UndertakerRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "TransferRequests")
public class TransferRequestDTO extends UndertakerRequestDTO<TransferRequestDTO>{
	
	private String transferTo;
}
