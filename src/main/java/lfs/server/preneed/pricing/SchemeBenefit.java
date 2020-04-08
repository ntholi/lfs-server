package lfs.server.preneed.pricing;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

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
@Inheritance(strategy=InheritanceType.SINGLE_TABLE)
public class SchemeBenefit extends AuditableEntity {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(columnDefinition = "SMALLINT UNSIGNED")
	private Integer id;
	
	private ItemType itemType;
	
	private Deductable deductable;
	
	private double discount;

	public enum Deductable {
		Free, Deductable
	}
	
	public boolean isFree() {
		if(deductable != null && deductable == Deductable.Free) {
			return true;
		}
		return false;
	}
}
