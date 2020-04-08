package lfs.server.preneed.pricing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lfs.server.audit.AuditableEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class DependentBenefit extends AuditableEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Column(nullable=false)
	private int minmumAge;
	
	@Column(nullable=false)
	private int maximumAge;
	
	@Column(nullable=false)
	private double coverAmount;
	
	@ManyToOne
	@JoinColumn(name="funeral_scheme_id")
	private FuneralScheme funeralScheme;
}
