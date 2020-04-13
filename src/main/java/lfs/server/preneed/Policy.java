package lfs.server.preneed;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lfs.server.audit.AuditableEntity;
import lfs.server.core.entity.District;
import lfs.server.core.entity.Gender;
import lfs.server.persistence.IdGenerator;
import lfs.server.preneed.pricing.FuneralScheme;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "policy_id",          
        strategy = "lfs.server.persistence.IdGenerator",
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_STRING)
})
public class Policy extends AuditableEntity<String> {
	
	@Id
	@GeneratedValue(generator = "policy_id")
	@Column(columnDefinition = "CHAR(10)")
	private String policyNumber;
	
	@NotBlank
	@Size(min = 2, max = 60)
	@Column(length = 60)
	private String names;
	
	@NotBlank
	@Size(min = 2, max = 50)
	@Column(length = 50)
	private String surname;
	
	@Column(columnDefinition="ENUM('MALE','FEMALE')")
	private Gender gender;
	
	@PastOrPresent
	private LocalDate dateOfBirth;
	
	@Nullable
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String phoneNumber;
	
	@Nullable
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String passportNumber;

	@Nullable
	@Size(min = 3, max = 50)
	@Column(length = 50)
	private String nationalIdNnumber;
	
	@Nullable
	@Column(length = 150)
	private String residentialArea;
	
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private District district;
	
	@Nullable
	@Column(length = 50)
	@Size(min = 3, max = 50)
	private String country;
	
	private boolean deceased;
	
	@NotNull
	@Column(nullable=false)
	private LocalDate registrationDate;
	
	@ManyToOne
	@JoinColumn(name="funeralScheme")
	private FuneralScheme funeralScheme;

	@Min(value = 0L, message = "{validation.number.positive}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private double premium;
	
	private boolean active;
	
	@OneToMany(mappedBy="policy", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<Dependent> dependents;
	
	@Override
	public String getId(){
		return policyNumber;
	}

//	public double getCoverAmountAtReg() { //TODO THIS SHOULD BE TURNED INTO A FIELD VARIABLE
//		int ageAtReg = getAgeAtRegistration();
//		Premium premium = FuneralSchemeDAO.getPremium(getFuneralScheme(), 
//				ageAtReg);
//		if(premium != null) {
//			return premium.getCoverAmount();
//		}
//		return 0;
//	}

	public int getAge() {
		return (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
	}
	
	protected int getAgeAtRegistration() {
		LocalDate regDate = registrationDate;
		if(regDate == null) {
			regDate = getCreatedAt().toLocalDate();
		}
		if(regDate == null) {
			regDate = LocalDate.now();
		}
		return (int) ChronoUnit.YEARS.between(dateOfBirth, regDate);
	}
}
