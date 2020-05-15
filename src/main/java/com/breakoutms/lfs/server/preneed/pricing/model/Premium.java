package com.breakoutms.lfs.server.preneed.pricing.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import org.hibernate.envers.Audited;

import com.breakoutms.lfs.server.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Audited
@Data @Builder
@EqualsAndHashCode(callSuper = true, exclude = "funeralScheme")
@AllArgsConstructor @NoArgsConstructor
public class Premium extends AuditableEntity<Integer> {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Min(value = 0L, message = "{validation.number.negative}") 
	@Max(255)
	@Column(nullable=false, columnDefinition = "TINYINT UNSIGNED")
	private int minmumAge;
	
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
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;

	public boolean isInAgeRange(int age) {
		return age >= minmumAge && age <= maximumAge;
	}
	
	public boolean isInAgeRange(LocalDate dateOfBirth) {
		long age = ChronoUnit.YEARS.between(dateOfBirth, LocalDate.now());
		return isInAgeRange((int) age);
	}
}
