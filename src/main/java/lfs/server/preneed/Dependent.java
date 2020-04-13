package lfs.server.preneed;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lfs.server.audit.AuditableEntity;
import lfs.server.persistence.IdGenerator;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "dependent_id",          
        strategy = "lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_STRING)
})
public class Dependent extends AuditableEntity<String>{

	@Id
	@GeneratedValue(generator = "dependent_id")
	@Column(columnDefinition = "CHAR(10)")
	private String id;
	
	@Size(min = 2, max = 60)
	@Column(length = 60)
    private String names;
	
	@Size(min = 2, max = 50)
	@Column(length = 50)
    private String surname;
	
	@PastOrPresent
    private LocalDate dateOfBirth;
	
	@Size(max = 30)
	@Column(length = 30)
	private String relationship;
	
	@Size(max = 50)
	@Column(length = 50)
    private String phoneNumber;
	
	private boolean deceased;
	
	@ManyToOne
	@JoinColumn(name="policy_id")
	private Policy policy;
	
	public int getAge() {
		int age = 0;
		if(dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		}
		return age;
	}
}
