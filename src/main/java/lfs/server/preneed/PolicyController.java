package lfs.server.preneed;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
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

import lfs.server.core.BaseController;
import lfs.server.core.DtoMapper;
import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/policies")
@AllArgsConstructor
public class PolicyController extends BaseController<Policy, PolicyDTO, String> {

	private final PolicyService service;
	private final PagedResourcesAssembler<PolicyDTO> pagedAssembler;
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<PolicyDTO> get(String id) {
		return getResponse(service.get(id), ExceptionSupplier.corpseNotFound(id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PolicyDTO>>> all(Pageable pageable) {
		Page<PolicyDTO> page = service.all(pageable)
				.map(o -> addLinks(o));
		return page.isEmpty()? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
				: new ResponseEntity<>(pagedAssembler.toModel(page),HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<PolicyDTO> save(@Valid @RequestBody Policy entity) {
		var model = addLinks(service.save(entity));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@Override
	protected PolicyDTO generateDTO(Policy policy) {
		return DtoMapper.INSTANCE.map(policy);
	}

}
