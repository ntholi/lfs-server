package com.breakoutms.lfs.server.preneed.deceased;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
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

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientDTO;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientViewModel;
import com.breakoutms.lfs.server.preneed.policy.PolicyController;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/policies")
@AllArgsConstructor
public class DeceasedClientController implements ViewModelController<DeceasedClient, DeceasedClientViewModel> {

	private final DeceasedClientService service;
	private final PagedResourcesAssembler<DeceasedClientViewModel> pagedAssembler;
	

	@GetMapping("{policyNumber}/deceased/{id}")
	public ResponseEntity<DeceasedClientViewModel> get(@PathVariable String policyNumber, @PathVariable Long id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Deceased Client", id));
	}
	
	@GetMapping("deceased")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.all(pageable));
	}
	
	@GetMapping("{policyNumber}/deceased")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientViewModel>>> all(@PathVariable String policyNumber, 
			Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.all(pageable));
	}

	@PostMapping("{policyNumber}/deceased")
	public ResponseEntity<DeceasedClientViewModel> save(@PathVariable String policyNumber,
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		
		return new ResponseEntity<>(
				toViewModel(service.save(entity, policyNumber)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("{policyNumber}/deceased/{id}")
	public ResponseEntity<DeceasedClientViewModel> update(@PathVariable String policyNumber,
			@PathVariable Long id, 
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
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
	public DeceasedClientViewModel toViewModel(DeceasedClient entity) {
		DeceasedClientViewModel viewModel = DeceasedClientMapper.INSTANCE.map(entity);
		val policyNumber = entity.getPolicy() != null ? entity.getPolicy().getPolicyNumber() : "";
		viewModel.add(linkTo(methodOn(getClass()).get(policyNumber, entity.getId())).withSelfRel());
		viewModel.add(linkTo(getClass()).slash(policyNumber).slash("deceasedClients").withRel("all"));
		viewModel.add(CommonLinks.branch(entity.getBranch()));
		String policyId = entity.getPolicy() != null? entity.getPolicy().getId(): null;
		viewModel.add(linkTo(methodOn(PolicyController.class).get(policyId)).withRel("policy"));
		return viewModel;
	}

}
