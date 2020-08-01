package com.breakoutms.lfs.server.undertaker.postmortem;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseController;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.undertaker.UndertakerRequestMapper;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequestDTO;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequestViewModel;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.UNDERTAKER+"/postmortem-request")
@AllArgsConstructor
public class PostmortemRequestController 
		implements ViewModelController<PostmortemRequest, PostmortemRequestViewModel> {

	private final PostmortemRequestService service;
	private final PagedResourcesAssembler<PostmortemRequestViewModel> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<PostmortemRequestViewModel> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("PostmortemRequest", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PostmortemRequestViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<PostmortemRequestViewModel> save(@Valid @RequestBody PostmortemRequestDTO dto) {
		PostmortemRequest entity = UndertakerRequestMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PostmortemRequestViewModel> update(@PathVariable Integer id, 
			@Valid @RequestBody PostmortemRequestDTO dto) {
		PostmortemRequest entity = UndertakerRequestMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@Override
	public PostmortemRequestViewModel toViewModel(PostmortemRequest entity) {
		PostmortemRequestViewModel dto = UndertakerRequestMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		Corpse corpse = entity.getCorpse();
		if(corpse != null) {
			String tagNo = corpse.getId();
			dto.add(linkTo(methodOn(CorpseController.class).get(tagNo)).withRel("corpse"));
		}
		return dto;
	}

}
