package lfs.server.mortuary;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

@Component
public class CorpseModelAssembler implements RepresentationModelAssembler<Corpse, EntityModel<Corpse>> {
	
	@Override
	public EntityModel<Corpse> toModel(Corpse corpse) {

		return new EntityModel<>(corpse,
				linkTo(methodOn(CorpseController.class).get(corpse.getTagNo())).withSelfRel(),
				linkTo(methodOn(CorpseController.class).all(0,20,"createdAt")).withRel("corpses"));
	}
}