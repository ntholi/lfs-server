package com.breakoutms.lfs.server.sales;

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

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesViewModel;
import com.breakoutms.lfs.server.security.Domain;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.SALES)
@AllArgsConstructor
public class SalesController implements ViewModelController<Sales, SalesViewModel> {

	private final SalesService service;
	private final PagedResourcesAssembler<SalesViewModel> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<SalesViewModel> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Sales", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<SalesViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<SalesViewModel> save(@Valid @RequestBody SalesDTO dto) {
		Sales entity = SalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SalesViewModel> update(@PathVariable Integer id, 
			@Valid @RequestBody SalesDTO dto) {
		Sales entity = SalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@Override
	public SalesViewModel toViewModel(Sales entity) {
		SalesViewModel dto = SalesMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return dto;
	}
}
