package com.breakoutms.lfs.server.mortuary.released.model;

import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.breakoutms.lfs.common.enums.ReleasedCorpseStatus;

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
	private Integer burialDetailsId;
	@NotBlank
	private String tagNo;
	@NotNull
	private LocalDateTime date;
	private String dressedBy;
	private String coffinedBy;
	private ReleasedCorpseStatus status;
}
