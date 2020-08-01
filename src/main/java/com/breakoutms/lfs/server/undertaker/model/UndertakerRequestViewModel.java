package com.breakoutms.lfs.server.undertaker.model;

import org.springframework.hateoas.RepresentationModel;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor @NoArgsConstructor
@EqualsAndHashCode(callSuper=false)
public class UndertakerRequestViewModel<T extends UndertakerRequestViewModel<T>>  extends RepresentationModel<T> {

	private Long id;

	private String tagNo;
	
	private boolean opened;
}
