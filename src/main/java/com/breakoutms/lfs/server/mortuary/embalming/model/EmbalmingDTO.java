package com.breakoutms.lfs.server.mortuary.embalming.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.enums.EmbalmingType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmbalmingDTO {

	private Integer id;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String embalmer;
	private EmbalmingType embalmingType;
	private double formalin;
}
