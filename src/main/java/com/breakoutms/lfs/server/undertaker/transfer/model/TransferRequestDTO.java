package com.breakoutms.lfs.server.undertaker.transfer.model;

import com.breakoutms.lfs.common.enums.RequestPerson;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TransferRequestDTO {
	
	private String transferTo;
}
