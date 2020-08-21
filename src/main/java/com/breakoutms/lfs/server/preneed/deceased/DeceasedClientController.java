package com.breakoutms.lfs.server.preneed.deceased;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
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
	

	@GetMapping("{policyNumber}/deceaseds/{id}")
	public ResponseEntity<DeceasedClientViewModel> get(@PathVariable String policyNumber, @PathVariable Long id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Deceased Client", id));
	}
	
	@GetMapping("deceaseds")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.all(pageable));
	}
	
	@GetMapping("{policyNumber}/deceaseds")
	public ResponseEntity<PagedModel<EntityModel<DeceasedClientViewModel>>> all(@PathVariable String policyNumber, 
			Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler, 
				service.all(pageable));
	}

	@PostMapping("{policyNumber}/deceaseds")
	public ResponseEntity<DeceasedClientViewModel> save(@PathVariable String policyNumber,
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		
		return new ResponseEntity<>(
				toViewModel(service.save(entity, policyNumber)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("{policyNumber}/deceaseds/{id}")
	public ResponseEntity<DeceasedClientViewModel> update(@PathVariable String policyNumber,
			@PathVariable Long id, 
			@Valid @RequestBody DeceasedClientDTO dto) {
		DeceasedClient entity = DeceasedClientMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
//	@GetMapping("{policyNumber}/deceaseds/inquire")
//	public ResponseEntity<EntityModel<DeceasedClientInquiry>> getDeceasedClientInquiry(
//			@PathVariable String policyNumber, 
//			@RequestParam(required = false) String period){
//		Period currentPeriod = Period.fromOrdinal(period);
//		if(currentPeriod == null) {
//			currentPeriod = Period.now();
//		}
//		DeceasedClientInquiry inquiry = service.getDeceasedClientInquiry(policyNumber, currentPeriod);
//		return ResponseEntity.ok(
//				EntityModel.of(inquiry,
//						linkTo(methodOn(PolicyController.class).get(policyNumber)).withRel("policy")));
//	}

	@Override
	public DeceasedClientViewModel toViewModel(DeceasedClient entity) {
		DeceasedClientViewModel viewModel = DeceasedClientMapper.INSTANCE.map(entity);
		val id = entity.getId();
		val deceasedClientId = entity.getId();
		val policyNumber = entity.getPolicy() != null ? entity.getPolicy().getPolicyNumber() : "";
		viewModel.add(linkTo(methodOn(getClass()).get(policyNumber, id)).withSelfRel());
		viewModel.add(linkTo(getClass()).slash(policyNumber).slash("deceasedClients").withRel("all"));
		viewModel.add(CommonLinks.branch(entity.getBranch()));
		String policyId = entity.getPolicy() != null? entity.getPolicy().getId(): null;
		viewModel.add(linkTo(methodOn(PolicyController.class).get(policyId)).withRel("policy"));
		return viewModel;
	}

}
