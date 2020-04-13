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

import lfs.server.audit.AuditableEntity;
import lfs.server.sales.items.ItemType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class FuneralSchemeBenefit extends AuditableEntity<Integer> {

	public enum Deductable {
		FREE, DEDUCTABLE
	}
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	@Column(columnDefinition = "TINYINT UNSIGNED")
	private ItemType itemType;
	
	@Column(columnDefinition="ENUM('FREE','DEDUCTABLE')")
	private Deductable deductable;
	
	@Column(precision=5, scale=4)
	@Digits(integer=1, fraction=4)
	private BigDecimal discount;
	
	@ManyToOne
	@JoinColumn(name="funeral_scheme_id", nullable = false)
	private FuneralScheme funeralScheme;

	
	public boolean isFree() {
		return (deductable != null && deductable == Deductable.FREE);
	}
}
