package lfs.server.mortuary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lfs.server.persistence.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "next_of_kin_id",          
        strategy = "lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
public class NextOfKin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "next_of_kin_id")
    private Integer id;
	
	@Column(length = 50)
    private String names;
    
	@Column(length = 50)
	private String surname;
	
	@Column(length = 50)
    private String relationship;
	
	@Column(length = 20)
    private String phoneNumber;
	
    private String phycialAddress;
    
    @ManyToOne(fetch = FetchType.LAZY) 
    @JoinColumn(name="corpse_id")
    @JsonIgnore
    private Corpse corpse;
}
