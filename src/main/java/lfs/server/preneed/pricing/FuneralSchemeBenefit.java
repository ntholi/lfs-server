package lfs.server.preneed.pricing;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;


@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor @NoArgsConstructor
public class FuneralSchemeBenefit extends SchemeBenefit {

	@ManyToOne
	@JoinColumn(name="funeral_scheme_id")
	private FuneralScheme funeralScheme;
}
