package com.breakoutms.lfs.server.transport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.persistence.IdGenerator;

import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data
@NoArgsConstructor
@GenericGenerator(
        name = "transport_id",          
        strategy = "com.breakoutms.lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_LONG)
})
public class Transport {

	@Id
	@GeneratedValue(generator = "transport_id")
	private Long id;
	private String placeHolder;
}
