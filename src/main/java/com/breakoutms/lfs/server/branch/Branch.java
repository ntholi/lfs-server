package com.breakoutms.lfs.server.branch;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Index;
import javax.persistence.Table;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotBlank;

import com.breakoutms.lfs.server.core.enums.District;
import com.breakoutms.lfs.server.util.BeanUtil;

import lombok.Data;

@Entity
@Table(indexes = {
        @Index(columnList = "name", name = "unique_branch_name", unique=true)
})
@Data
public class Branch {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@NotBlank
	@Column(nullable = false, length = 50)
	private String name;
	
	@Enumerated
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private District district;
	
	@Column(nullable = false, unique = true,
			columnDefinition = "SMALLINT")
	@Digits(integer = 4, fraction = 0)
	private short syncNumber;
    
	public static Branch current() {
		return BranchHolder.INSTANCE;
	}
	
    private static class BranchHolder {
    	//Initialization-on-demand holder idiom
        private static final Branch INSTANCE = BeanUtil.getBean(CurrentBranch.class).get();
    }
}
