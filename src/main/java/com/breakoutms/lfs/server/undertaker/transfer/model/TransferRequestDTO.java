package com.breakoutms.lfs.server.undertaker.transfer.model;

import org.springframework.hateoas.server.core.Relation;

import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequestDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=true)
@Relation(collectionRelation = "TransferRequests")
public class TransferRequestDTO extends MortuaryRequestDTO<TransferRequestDTO>{
	
	private String transferTo;
}
