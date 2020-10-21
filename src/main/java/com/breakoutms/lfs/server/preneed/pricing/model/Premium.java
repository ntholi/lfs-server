package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.persistence.IdGenerator;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Audited
@Data @Builder
@AllArgsConstructor @NoArgsConstructor
@GenericGenerator(
        name = "premium_id",          
        strategy = IdGenerator.STRATEGY,
        parameters = {
	            @Parameter(name = IdGenerator.ID_TYPE_PARAM, value = IdGenerator.ID_TYPE_INTEGER)
})
public class Premium {

	@Id
	@GeneratedValue(generator = "premium_id")
	private Integer id;
	
	@Min(value = 0L, message = "{validation.number.negative}") 
	@Max(255)
	@Column(nullable=false, columnDefinition = "TINYINT UNSIGNED")
	private int minimumAge;
	
	@Min(value = 0L, message = "{validation.number.negative}") 
	@Max(255)
	@Column(nullable=false, columnDefinition = "TINYINT UNSIGNED")
	private int maximumAge;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 6, fraction = 2)
	@Column(nullable=false, precision = 8, scale = 2)
	private BigDecimal premiumAmount;
	
	@Min(value = 0L, message = "{validation.number.negative}")
	@Digits(integer = 8, fraction = 2)
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal coverAmount;
	
	@ToString.Exclude
	@JsonIgnore
	@EqualsAndHashCode.Exclude
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;

	public boolean isInAgeRange(int age) {
		return age >= minimumAge && age <= maximumAge;
	}
	
	public boolean isInAgeRange(LocalDate dateOfBirth) {
		long age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		return isInAgeRange((int) age);
	}
}
