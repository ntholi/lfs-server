package com.breakoutms.lfs.server.preneed.pricing;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

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
import com.breakoutms.lfs.server.core.DtoMapper;
import com.breakoutms.lfs.server.core.EntityController;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.user.Domain;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/funeral-schemes")
@AllArgsConstructor
public class FuneralSchemeController implements EntityController<FuneralScheme, FuneralSchemeDTO> {

	private final FuneralSchemeService service;
	private final PagedResourcesAssembler<FuneralSchemeDTO> pagedAssembler;
	

	@GetMapping("/{id}")
	public ResponseEntity<FuneralSchemeDTO> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<FuneralSchemeDTO>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<FuneralSchemeDTO> save(@Valid @RequestBody FuneralScheme entity) {
		return new ResponseEntity<>(
				createDtoWithLinks(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FuneralSchemeDTO> update(@PathVariable Integer id, @Valid @RequestBody FuneralScheme entity) {
		return new ResponseEntity<>(
				createDtoWithLinks(service.update(id, entity)), 
				HttpStatus.OK
		);
	}

	
	@GetMapping("/{id}/penalty-deductibles")
	public ResponseEntity<List<PenaltyDeductible>> getPenaltyDeductibles(@PathVariable Integer id) {
		var list = service.getPenaltyDeductibles(id);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}

	@GetMapping("/{id}/funeral-scheme-benefit")
	public ResponseEntity<List<FuneralSchemeBenefit>> getFuneralSchemeBenefit(@PathVariable Integer id) {
		var list = service.getFuneralSchemeBenefit(id);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}

	@GetMapping("/{id}/dependent-benefits")
	public ResponseEntity<List<DependentBenefit>> getDependentBenefits(@PathVariable Integer id) {
		var list = service.getDependentBenefits(id);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}

	@GetMapping("/{id}/premiums")
	public ResponseEntity<List<Premium>> getPremiums(@PathVariable Integer id) {
		var list = service.getPremiums(id);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}
	
	@Override
	public FuneralSchemeDTO createDtoWithLinks(FuneralScheme entity) {
		FuneralSchemeDTO dto = DtoMapper.INSTANCE.map(entity);
		var id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		dto.add(linkTo(methodOn(getClass()).getPremiums(id)).withRel("premiums"));
		dto.add(linkTo(methodOn(getClass()).getDependentBenefits(id)).withRel("dependentBenefits"));
		dto.add(linkTo(methodOn(getClass()).getFuneralSchemeBenefit(id)).withRel("funeralSchemeBenefit"));
		dto.add(linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withRel("penaltyDeductibles"));
		return dto;
	}
}
