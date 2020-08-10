package com.breakoutms.lfs.server.undertaker.transfer.model;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
	
	private Integer id;
	@NotNull
	private String tagNo;
	@NotNull
	private String transferTo;
	private boolean seen;
}
