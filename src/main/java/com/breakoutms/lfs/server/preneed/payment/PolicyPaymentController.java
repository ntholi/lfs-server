package com.breakoutms.lfs.server.preneed.payment;

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
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentViewModel;
import com.breakoutms.lfs.server.security.Domain;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/policies/payments")
@AllArgsConstructor
public class PolicyPaymentController implements ViewModelController<PolicyPayment, PolicyPaymentViewModel> {

	private final PolicyPaymentService service;
	private final PagedResourcesAssembler<PolicyPaymentViewModel> pagedAssembler;
	

	@GetMapping("/{id}")
	public ResponseEntity<PolicyPaymentViewModel> get(@PathVariable Long id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PolicyPaymentViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<PolicyPaymentViewModel> save(@Valid @RequestBody PolicyPaymentDTO dto) {
		PolicyPayment entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PolicyPaymentViewModel> update(@PathVariable Long id, 
			@Valid @RequestBody PolicyPaymentDTO dto) {
		PolicyPayment entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}

	@Override
	public PolicyPaymentViewModel toViewModel(PolicyPayment entity) {
		PolicyPaymentViewModel dto = PreneedMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return dto;
	}

}
