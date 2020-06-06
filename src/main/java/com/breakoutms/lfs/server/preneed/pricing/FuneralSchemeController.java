package com.breakoutms.lfs.server.preneed.pricing;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
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

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefitViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefitViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductibleViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumViewModel;
import com.breakoutms.lfs.server.security.Domain;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/funeral-schemes")
@AllArgsConstructor
public class FuneralSchemeController implements ViewModelController<FuneralScheme, FuneralSchemeViewModel> {

	private static final String FUNERAL_SCHEME = "funeralScheme";
	private final FuneralSchemeService service;
	private final PagedResourcesAssembler<FuneralSchemeViewModel> pagedAssembler;

	//TODO: ADD DELETE END-POINT

	@GetMapping("/{id}")
	public ResponseEntity<FuneralSchemeViewModel> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<FuneralSchemeViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<FuneralSchemeViewModel> save(@Valid @RequestBody FuneralSchemeDTO dto) {
		FuneralScheme entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FuneralSchemeViewModel> update(@PathVariable Integer id, 
			@Valid @RequestBody FuneralSchemeDTO dto) {
		FuneralScheme entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}

	@GetMapping("/{id}/premiums")
	public ResponseEntity<CollectionModel<PremiumViewModel>> getPremiums(@PathVariable Integer id) {
		List<PremiumViewModel> list = new ArrayList<>();
		for (var entity: service.getPremiums(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = new CollectionModel<>(list,
				linkTo(methodOn(getClass()).getPremiums(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}/dependent-benefits")
	public ResponseEntity<CollectionModel<DependentBenefitViewModel>> getDependentBenefits(@PathVariable Integer id) {
		List<DependentBenefitViewModel> list = new ArrayList<>();
		for (var entity: service.getDependentBenefits(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = new CollectionModel<>(list,
				linkTo(methodOn(getClass()).getDependentBenefits(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("/{id}/funeral-scheme-benefits")
	public ResponseEntity<CollectionModel<FuneralSchemeBenefitViewModel>> getFuneralSchemeBenefits(@PathVariable Integer id) {
		List<FuneralSchemeBenefitViewModel> list = new ArrayList<>();
		for (var entity: service.getFuneralSchemeBenefits(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = new CollectionModel<>(list,
				linkTo(methodOn(getClass()).getFuneralSchemeBenefits(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}

	@GetMapping("/{id}/penalty-deductibles")
	public ResponseEntity<CollectionModel<PenaltyDeductibleViewModel>> getPenaltyDeductibles(@PathVariable Integer id) {
		List<PenaltyDeductibleViewModel> list = new ArrayList<>();
		for (var entity: service.getPenaltyDeductibles(id)) {
			var dto = PreneedMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(id)).withRel(FUNERAL_SCHEME));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = new CollectionModel<>(list,
				linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	
	@Override
	public FuneralSchemeViewModel toViewModel(FuneralScheme entity) {
		FuneralSchemeViewModel dto = PreneedMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		dto.add(linkTo(methodOn(getClass()).getPremiums(id)).withRel("premiums"));
		dto.add(linkTo(methodOn(getClass()).getDependentBenefits(id)).withRel("dependentBenefits"));
		dto.add(linkTo(methodOn(getClass()).getFuneralSchemeBenefits(id)).withRel("funeralSchemeBenefits"));
		dto.add(linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withRel("penaltyDeductibles"));
		return dto;
	}
}
