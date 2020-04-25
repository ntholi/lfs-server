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
import com.breakoutms.lfs.server.core.EntityController;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeDTO;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeViewModel;
import com.breakoutms.lfs.server.preneed.pricing.model.PremiumViewModel;
import com.breakoutms.lfs.server.user.Domain;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/funeral-schemes")
@AllArgsConstructor
public class FuneralSchemeController implements EntityController<FuneralScheme, FuneralSchemeViewModel> {

	private static final String FUNERAL_SCHEME = "funeralScheme";
	private final FuneralSchemeService service;
	private final PagedResourcesAssembler<FuneralSchemeViewModel> pagedAssembler;
	

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
				createDtoWithLinks(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<FuneralSchemeViewModel> update(@PathVariable Integer id, 
			@Valid @RequestBody FuneralSchemeDTO dto) {
		FuneralScheme entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				createDtoWithLinks(service.update(id, entity)), 
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
		
		CollectionModel<PremiumViewModel> result = new CollectionModel<>(list,
				linkTo(methodOn(getClass()).getPremiums(id)).withSelfRel());
		return ResponseEntity.ok(result);
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
	
	
	@Override
	public FuneralSchemeViewModel createDtoWithLinks(FuneralScheme entity) {
		FuneralSchemeViewModel dto = PreneedMapper.INSTANCE.map(entity);
		var id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		dto.add(linkTo(methodOn(getClass()).getPremiums(id)).withRel("premiums"));
		dto.add(linkTo(methodOn(getClass()).getDependentBenefits(id)).withRel("dependentBenefits"));
		dto.add(linkTo(methodOn(getClass()).getFuneralSchemeBenefit(id)).withRel("funeralSchemeBenefit"));
		dto.add(linkTo(methodOn(getClass()).getPenaltyDeductibles(id)).withRel("penaltyDeductibles"));
		return dto;
	}
}
