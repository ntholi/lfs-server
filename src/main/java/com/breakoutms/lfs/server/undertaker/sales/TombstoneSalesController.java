package com.breakoutms.lfs.server.undertaker.sales;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseController;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSales;
import com.breakoutms.lfs.server.undertaker.sales.model.TombstoneSalesDTO;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.UNDERTAKER+"/sales")
@AllArgsConstructor
public class TombstoneSalesController 
		implements ViewModelController<TombstoneSales, TombstoneSalesDTO> {

	private final TombstoneSalesService service;
	private final PagedResourcesAssembler<TombstoneSalesDTO> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<TombstoneSalesDTO> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("TombstoneSales", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<TombstoneSalesDTO>>> all(
			@SearchSpec Specification<TombstoneSales> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}

	@PostMapping
	public ResponseEntity<TombstoneSalesDTO> save(@Valid @RequestBody TombstoneSalesDTO dto) {
		TombstoneSales entity = TombstoneSalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<TombstoneSalesDTO> update(@PathVariable Integer id, 
			@Valid @RequestBody TombstoneSalesDTO dto) {
		TombstoneSales entity = TombstoneSalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@Override
	public TombstoneSalesDTO toViewModel(TombstoneSales entity) {
		TombstoneSalesDTO dto = TombstoneSalesMapper.INSTANCE.map(entity);
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
