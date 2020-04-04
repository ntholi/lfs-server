package lfs.server.mortuary;


import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import lfs.server.branch.BranchController;

@Component
public class CorpseModelAssembler implements RepresentationModelAssembler<Corpse, EntityModel<CorpseResponseDTO>> {
	
	@Override
	public EntityModel<CorpseResponseDTO> toModel(Corpse corpse) {
		CorpseResponseDTO dto = CorpseMapper.INSTANCE.toDto(corpse);
		String tagNo = corpse.getTagNo();
		OtherMortuary otherMortuaryId = corpse.getTransferredFrom();
		var model = new EntityModel<>(dto,
				linkTo(methodOn(CorpseController.class).get(tagNo)).withSelfRel(),
				linkTo(CorpseController.class).withRel("corpses"));
		model.add(linkTo(BranchController.class).slash(corpse.getBranch().getId()).withRel("branch"));
		model.add(linkTo(methodOn(CorpseController.class).getNextOfKins(tagNo)).withRel("nextOfKins"));
		if(otherMortuaryId != null) {
			model.add(linkTo(methodOn(CorpseController.class).getTransforedFrom(otherMortuaryId.getId()))
					.withRel("transferredFrom"));
		}
		return model;
	}
}