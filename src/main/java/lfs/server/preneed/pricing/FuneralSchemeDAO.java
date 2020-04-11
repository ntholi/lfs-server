package lfs.server.preneed.pricing;

import java.time.LocalDate;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "funeralSchemes")
public class FuneralSchemeDAO extends RepresentationModel<FuneralSchemeDAO> {

	private String name;
	private LocalDate date = LocalDate.now();
}
