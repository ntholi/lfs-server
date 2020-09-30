package com.breakoutms.lfs.server.preneed.pricing;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
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
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefitDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefitDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductibleDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumDTO;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.ADMIN+"/funeral-schemes")
@AllArgsConstructor
public class FuneralSchemeController implements ViewModelController<FuneralScheme, FuneralSchemeDTO> {

	private static final String FUNERAL_SCHEME = "funeralScheme";
	private final FuneralSchemeService service;
	private final PagedResourcesAssembler<FuneralSchemeDTO> pagedAssembler;

	//TODO: ADD DELETE END-POINT

	@GetMapping("/{id}")
	public ResponseEntity<FuneralSchemeDTO> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<FuneralSchemeDTO>>> all(
			@SearchSpec Specification<FuneralScheme> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}

	@PostMapping
	public ResponseEntity<FuneralSchemeDTO> save(@Valid @RequestBody FuneralSchemeDTO dto) {
		FuneralScheme entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FuneralSchemeDTO> update(@PathVariable Integer id, 
			@Valid @RequestBody FuneralSchemeDTO dto) {
		FuneralScheme entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}

	@GetMapping("/{id}/premiums")
	public ResponseEntity<CollectionModel<PremiumDTO>> getPremiums(@PathVariable Integer id) {
		List<PremiumDTO> list = new ArrayList<>();
		for (var entity: service.getPremiums(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = CollectionModel.of(list,
				linkTo(methodOn(getClass()).getPremiums(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}/dependent-benefits")
	public ResponseEntity<CollectionModel<DependentBenefitDTO>> getDependentBenefits(@PathVariable Integer id) {
		List<DependentBenefitDTO> list = new ArrayList<>();
		for (var entity: service.getDependentBenefits(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = CollectionModel.of(list,
				linkTo(methodOn(getClass()).getDependentBenefits(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}/funeral-scheme-benefits")
	public ResponseEntity<CollectionModel<FuneralSchemeBenefitDTO>> getFuneralSchemeBenefits(@PathVariable Integer id) {
		List<FuneralSchemeBenefitDTO> list = new ArrayList<>();
		for (var entity: service.getFuneralSchemeBenefits(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = CollectionModel.of(list,
				linkTo(methodOn(getClass()).getFuneralSchemeBenefits(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}/penalty-deductibles")
	public ResponseEntity<CollectionModel<PenaltyDeductibleDTO>> getPenaltyDeductibles(@PathVariable Integer id) {
		List<PenaltyDeductibleDTO> list = new ArrayList<>();
		for (var entity: service.getPenaltyDeductibles(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = CollectionModel.of(list,
				linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@Override
	public FuneralSchemeDTO toViewModel(FuneralScheme entity) {
		FuneralSchemeDTO dto = PreneedMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		dto.add(linkTo(methodOn(getClass()).getPremiums(id)).withRel("premiums"));
		dto.add(linkTo(methodOn(getClass()).getDependentBenefits(id)).withRel("dependentBenefits"));
		dto.add(linkTo(methodOn(getClass()).getFuneralSchemeBenefits(id)).withRel("funeralSchemeBenefits"));
		dto.add(linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withRel("penaltyDeductibles"));
		return dto;
	}
}
