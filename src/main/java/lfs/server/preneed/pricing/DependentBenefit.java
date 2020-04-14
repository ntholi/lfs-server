package lfs.server.preneed.pricing;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

import lfs.server.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data @Builder
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class DependentBenefit extends AuditableEntity<Integer> {

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
	@Digits(integer = 8, fraction = 2)
	@Column(nullable=false, precision = 10, scale = 2)
	private BigDecimal coverAmount;
	
	@ManyToOne
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;
}
