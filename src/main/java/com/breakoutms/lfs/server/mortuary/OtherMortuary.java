package com.breakoutms.lfs.server.mortuary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.breakoutms.lfs.server.persistence.IdGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@GenericGenerator(
        name = "other_mortuary_id",          
        strategy = "com.breakoutms.lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
public class OtherMortuary {

	@Id
	@GeneratedValue(generator = "other_mortuary_id")
	private Integer id;
	@Column(length = 50)
	private String name;
	
	public OtherMortuary(String name) {
		this.name = name;
	}
}
