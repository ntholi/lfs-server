package com.breakoutms.lfs.server.mortuary;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.model.Corpse;
import com.breakoutms.lfs.server.mortuary.model.CorpseViewModel;
import com.breakoutms.lfs.server.security.Domain;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.MORTUARY)
@AllArgsConstructor
public class CorpseController implements ViewModelController<Corpse, CorpseViewModel> {
	
	private final CorpseService service;
	private final PagedResourcesAssembler<CorpseViewModel> pagedAssembler;


	@GetMapping("/{id}")
	public ResponseEntity<CorpseViewModel> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Corpse", id));
	}

	@GetMapping 
	public ResponseEntity<PagedModel<EntityModel<CorpseViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@Override
	public CorpseViewModel toViewModel(Corpse entity) {
		CorpseViewModel viewModel = CorpseMapper.INSTANCE.map(entity);
		val id = entity.getId();
		viewModel.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return viewModel;
	}
}
