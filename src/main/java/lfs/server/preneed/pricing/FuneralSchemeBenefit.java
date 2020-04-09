package lfs.server.preneed.pricing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

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
	
	private ItemType itemType;
	
	private Deductable deductable;
	
	private double discount;
	
	@ManyToOne
	@JoinColumn(name="funeral_scheme_id")
	private FuneralScheme funeralScheme;

	
	public boolean isFree() {
		return (deductable != null && deductable == Deductable.FREE);
	}
}
