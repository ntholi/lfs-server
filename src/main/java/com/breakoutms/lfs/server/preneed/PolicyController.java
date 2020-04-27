package com.breakoutms.lfs.server.preneed;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.EntityController;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.model.PolicyViewModel;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/policies")
@AllArgsConstructor
public class PolicyController implements EntityController<Policy, PolicyViewModel> {

	private final PolicyService service;
	private final PagedResourcesAssembler<PolicyViewModel> pagedAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<PolicyViewModel> get(String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PolicyViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}
	
	@PostMapping
	public ResponseEntity<PolicyViewModel> save(@Valid @RequestBody PolicyDTO dto) {
		Policy entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				createDtoWithLinks(service.save(entity, dto.getFuneralScheme())), 
				HttpStatus.CREATED
		);
	}

	@Override
	public PolicyViewModel createDtoWithLinks(Policy entity) {
		var dto = PreneedMapper.INSTANCE.map(entity);
		var id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return dto;
	}

}
