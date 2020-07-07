package com.breakoutms.lfs.server.mortuary.released.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import com.sun.istack.NotNull;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleasedCorpseDTO {

	private Integer id;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String dressedBy;
	private String coffinedBy;
}
