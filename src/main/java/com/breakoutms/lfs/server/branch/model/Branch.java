package com.breakoutms.lfs.server.branch.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.common.enums.District;

import lombok.Data;

@Entity
@Audited
@Table(indexes = {
        @Index(columnList = "name", name = "unique_branch_name", unique=true)
})
@Data
public class Branch implements com.breakoutms.lfs.server.core.Entity<Integer>{

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@NotBlank
	@Column(nullable = false, unique = true, length = 50)
	private String name;
	
	@Enumerated(EnumType.STRING)
	@Column(length = 50)
	private District district;
	
	@Column(nullable = false, unique = true,
			columnDefinition = "SMALLINT")
	@Digits(integer = 4, fraction = 0)
	@GeneratedValue
	private Integer syncNumber;
}
