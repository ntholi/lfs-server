package com.breakoutms.lfs.server.mortuary.request.model;

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
public class MortuaryRequestDTO<T extends MortuaryRequestDTO<T>>  extends RepresentationModel<T> {

	private Long id;
	@NotNull
	private String tagNo;
	private boolean seen;
	private boolean processed;
}
