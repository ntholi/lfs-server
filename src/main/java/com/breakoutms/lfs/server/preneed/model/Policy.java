package com.breakoutms.lfs.server.preneed.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import javax.annotation.Nullable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
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
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import com.breakoutms.lfs.server.core.entity.District;
import com.breakoutms.lfs.server.core.entity.Gender;
import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
		name = "policy_id",          
		strategy = IdGenerator.STRATEGY,
		parameters = {
				@Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_STRING)
})
@SQLDelete(sql = "UPDATE policy SET deleted=true WHERE id=?")
@Where(clause = AuditableEntity.CLAUSE)
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

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('MALE','FEMALE')")
	private Gender gender;

	@PastOrPresent
	@NotNull
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
	@Size(min = 3, max = 40)
	@Column(length = 40)
	private String nationalIdNumber;

	@Nullable
	@Column(length = 150)
	@Size(min = 2, max = 150)
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

	@ManyToOne(fetch = FetchType.LAZY)
	private FuneralScheme funeralScheme;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal premiumAmount;

	@NotNull
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal coverAmount;

	@Enumerated(EnumType.STRING)
	@Column(columnDefinition="ENUM('ACTIVE','WAITING_PERIOD','DEACTIVATED') DEFAULT 'ACTIVE'")
	private PolicyStatus status;

	@OneToMany(mappedBy="policy", 
			cascade=CascadeType.ALL, 
			orphanRemoval=true)
	private List<Dependent> dependents;

	public Policy(String policyNumber, PolicyStatus status) {
		this.policyNumber = policyNumber;
		this.status = status;
	}
	
	@Override
	public String getId(){
		return policyNumber;
	}

	public void setAge(int age) {
		this.dateOfBirth = LocalDate.now().minusYears(age);
	}
	
	@JsonIgnore
	public int getAge() {
		Integer age = null;
		if(dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		}
		return age;
	}
	
	@JsonIgnore
	public Integer getAgeAtRegistration() {
		Integer age = null;
		LocalDate regDate = registrationDate;
		if(regDate == null && createdAt != null) {
			regDate = createdAt.toLocalDate();
		}
		if(regDate == null) {
			regDate = LocalDate.now();
		}

		if(dateOfBirth != null) {
			age = (int) ChronoUnit.YEARS.between(dateOfBirth, regDate);
		}
		return age;
	}
}
