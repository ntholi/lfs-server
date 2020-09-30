package com.breakoutms.lfs.server.undertaker.model;

import javax.validation.constraints.NotNull;

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
public class UndertakerRequestDTO<T extends UndertakerRequestDTO<T>>  extends RepresentationModel<T> {

	private Long id;
	@NotNull
	private String tagNo;
	private boolean seen;
	private boolean processed;
}
