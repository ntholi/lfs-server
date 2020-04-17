package lfs.server.preneed;

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

import lfs.server.core.CommonLinks;
import lfs.server.core.DtoMapper;
import lfs.server.core.EntityController;
import lfs.server.core.ResponseHelper;
import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/policies")
@AllArgsConstructor
public class PolicyController implements EntityController<Policy, PolicyDTO> {

	private final PolicyService service;
	private final PagedResourcesAssembler<PolicyDTO> pagedAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<PolicyDTO> get(String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PolicyDTO>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}
	
	@PostMapping
	public ResponseEntity<PolicyDTO> save(@Valid @RequestBody Policy entity) {
		return new ResponseEntity<>(
				createDtoWithLinks(service.save(entity)), 
				HttpStatus.CREATED
		);
	}

	@Override
	public PolicyDTO createDtoWithLinks(Policy entity) {
		var dto = DtoMapper.INSTANCE.map(entity);
		var id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return dto;
	}

}
