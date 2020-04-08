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
public class PenaltyDeductable extends AuditableEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	private int months;
	private double amount;
	@ManyToOne 	@JoinColumn(name="funeralScheme")
	private FuneralScheme funeralScheme;
	
	public PenaltyDeductable(int months, Double amount) {
		this.months = months;
		this.amount = amount;
	}
}
