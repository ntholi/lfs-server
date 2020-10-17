package com.breakoutms.lfs.server.branch;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.branch.model.BranchDTO;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/branches")
@AllArgsConstructor
public class BranchController implements ViewModelController<Branch, BranchDTO>{

	private final BranchService service;
	private final PagedResourcesAssembler<BranchDTO> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<BranchDTO> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Branch", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<BranchDTO>>> all(
			@SearchSpec Specification<Branch> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}

	@PostMapping
	public ResponseEntity<BranchDTO> save(@Valid @RequestBody BranchDTO dto) {
		Branch entity = BranchMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<BranchDTO> update(@PathVariable Integer id, 
			@Valid @RequestBody BranchDTO dto) {
		Branch entity = BranchMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@Override
	public BranchDTO toViewModel(Branch entity) {
		var dto = BranchMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinks(getClass(), id));
		return dto;
	}
}
