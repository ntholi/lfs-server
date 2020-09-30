package com.breakoutms.lfs.server.preneed.deceased;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientDTO;
import com.breakoutms.lfs.server.preneed.deceased.model.Payout;
import com.breakoutms.lfs.server.preneed.policy.PolicyController;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/policies")
@AllArgsConstructor
public class DeceasedClientController implements ViewModelController<DeceasedClient, DeceasedClientDTO> {

	private final DeceasedClientService service;
	private final PagedResourcesAssembler<DeceasedClientDTO> pagedAssembler;
	

	@GetMapping("{policyNumber}/deceased/{id}")
	public ResponseEntity<DeceasedClientDTO> get(@PathVariable String policyNumber, @PathVariable Long id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Deceased Client", id));
	}
	
	@GetMapping("deceased")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientDTO>>> all(
			@SearchSpec Specification<DeceasedClient> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.search(specs, pageable));
	}
	
	@GetMapping("{policyNumber}/deceased")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientDTO>>> all(@PathVariable String policyNumber, 
			Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.all(pageable));
	}

	@PostMapping("{policyNumber}/deceased")
	public ResponseEntity<DeceasedClientDTO> save(@PathVariable String policyNumber,
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		
		return new ResponseEntity<>(
				toViewModel(service.save(entity, policyNumber)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("{policyNumber}/deceased/{id}")
	public ResponseEntity<DeceasedClientDTO> update(@PathVariable String policyNumber,
			@PathVariable Long id, 
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("{policyNumber}/payout")
	public ResponseEntity<Payout> getPayout(@PathVariable String policyNumber){
		return ResponseEntity.ok(service.getPayout(policyNumber));
	}
	
	@GetMapping("/deceased-client-lookup")
	public ResponseEntity<CollectionModel<CorpseLookupProjection>> lookup(String policyNumber) {
		if(StringUtils.isBlank(policyNumber)) {
			return ResponseEntity.noContent()
					.build();
		}
		var list = service.lookup(policyNumber);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(CollectionModel.of(list));
	}

	@Override
	public DeceasedClientDTO toViewModel(DeceasedClient entity) {
		DeceasedClientDTO dto = DeceasedClientMapper.INSTANCE.map(entity);
		val policyNumber = entity.getPolicy() != null ? entity.getPolicy().getPolicyNumber() : "";
		dto.add(linkTo(methodOn(getClass()).get(policyNumber, entity.getId())).withSelfRel());
		dto.add(linkTo(getClass()).slash(policyNumber).slash("deceasedClients").withRel("all"));
		dto.add(CommonLinks.branch(entity.getBranch()));
		String policyId = entity.getPolicy() != null? entity.getPolicy().getId(): null;
		dto.add(linkTo(methodOn(PolicyController.class).get(policyId)).withRel("policy"));
		return dto;
	}

}
