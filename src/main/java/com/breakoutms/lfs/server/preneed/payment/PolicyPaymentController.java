package com.breakoutms.lfs.server.preneed.payment;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.common.enums.PolicyPaymentType;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.payment.model.Period;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPayment;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentDetailsDTO;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentInquiry;
import com.breakoutms.lfs.server.preneed.payment.model.PolicyPaymentProjection;
import com.breakoutms.lfs.server.preneed.payment.report.PolicyPaymentReportService;
import com.breakoutms.lfs.server.preneed.policy.PolicyController;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/policies")
@AllArgsConstructor
public class PolicyPaymentController implements ViewModelController<PolicyPayment, PolicyPaymentDetailsDTO> {

	private final PolicyPaymentService service;
	private final PolicyPaymentReportService reportsService;
	private final PagedResourcesAssembler<PolicyPaymentProjection> pagedAssembler;
	

	@GetMapping("{policyNumber}/payments/{id}")
	public ResponseEntity<PolicyPaymentDetailsDTO> get(@PathVariable String policyNumber, @PathVariable Long id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Policy Payment", id));
	}
	
	@GetMapping("payments")
	public ResponseEntity<PagedModel<EntityModel<PolicyPaymentProjection>>> all(
			@SearchSpec Specification<PolicyPayment> specs, Pageable pageable) {
		var response = pagedAssembler.toModel(service.search(specs, pageable));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@GetMapping("{policyNumber}/payments")
	public ResponseEntity<PagedModel<EntityModel<PolicyPaymentDetailsDTO>>> all(@PathVariable String policyNumber, 
			Pageable pageable) {
		//TODO: implement this guy
		return null;
	}

	@PostMapping("{policyNumber}/payments")
	public ResponseEntity<PolicyPaymentDetailsDTO> save(@PathVariable String policyNumber,
			@Valid @RequestBody PolicyPaymentDTO dto) {
		PolicyPayment entity = PolicyPaymentMapper.INSTANCE.map(dto);
		validate(entity);
		return new ResponseEntity<>(
				toViewModel(service.save(entity, policyNumber)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("{policyNumber}/payments/{id}")
	public ResponseEntity<PolicyPaymentDetailsDTO> update(@PathVariable String policyNumber,
			@PathVariable Long id, 
			@Valid @RequestBody PolicyPaymentDTO dto) {
		PolicyPayment entity = PolicyPaymentMapper.INSTANCE.map(dto);
		validate(entity);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("{policyNumber}/payments/{policyPaymentId}/payment-details")
	public ResponseEntity<CollectionModel<PolicyPaymentDetailsDTO>> getPaymentDetails(
			@PathVariable String policyNumber, @PathVariable Long policyPaymentId) {
		List<PolicyPaymentDetailsDTO> list = new ArrayList<>();
		for (var entity: service.getPaymentDetails(policyPaymentId)) {
			var dto = PolicyPaymentMapper.INSTANCE.map(entity);
			dto.add(linkTo(methodOn(getClass()).get(policyNumber, policyPaymentId)).withRel("policyPayment"));
			list.add(dto);
		}
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		
		var result = CollectionModel.of(list,
				linkTo(methodOn(getClass()).getPaymentDetails(policyNumber, policyPaymentId)).withSelfRel());
		return ResponseEntity.ok(result);
	}
	
	@GetMapping("{policyNumber}/payments/inquire")
	public ResponseEntity<EntityModel<PolicyPaymentInquiry>> getPolicyPaymentInquiry(
			@PathVariable String policyNumber, 
			@RequestParam(required = false) String period){
		Period currentPeriod = Period.fromOrdinal(period);
		if(currentPeriod == null) {
			currentPeriod = Period.now();
		}
		PolicyPaymentInquiry inquiry = service.getPolicyPaymentInquiry(policyNumber, currentPeriod);
		return ResponseEntity.ok(
				EntityModel.of(inquiry,
						linkTo(methodOn(PolicyController.class).get(policyNumber)).withRel("policy")));
	}

	@GetMapping("payments/reports/payment-report")
	public Map<String, Object> reports(int reportType, String from, String to, 
			@RequestParam(required = false) Integer branch, 
			@RequestParam(required = false) Integer user) {
		LocalDate fromDate = StringUtils.isNotBlank(from)? LocalDate.parse(from): null;
		LocalDate toDate = StringUtils.isNotBlank(to)? LocalDate.parse(to): null;
		Map<String, Object> res = null;
		if(reportType <= 0) {
			res = reportsService.getCollections(fromDate, toDate, branch, user);
		}
		else if(reportType == 1){
			res = reportsService.getPolicyPaymentReport(fromDate, toDate, branch, user);
		}
		return res;
	}
	
	
	private void validate(PolicyPayment entity) {
		for(var details : entity.getPolicyPaymentDetails()) {
			if(details.getType() == PolicyPaymentType.PREMIUM) {
				if(details.getPeriod() == null) {
					throw new NullPointerException("Period for premium cannot be null");
				}
			}
		}
	}
	@Override
	public PolicyPaymentDetailsDTO toViewModel(PolicyPayment entity) {
		PolicyPaymentDetailsDTO dto = PolicyPaymentMapper.INSTANCE.map(entity);
		val id = entity.getId();
		val policyPaymentId = entity.getId();
		val policyNumber = entity.getPolicy() != null ? entity.getPolicy().getPolicyNumber() : "";
		dto.add(linkTo(methodOn(getClass()).get(policyNumber, id)).withSelfRel());
		dto.add(linkTo(getClass()).slash(policyNumber).slash("payments").withRel("all"));
		dto.add(linkTo(methodOn(getClass()).getPaymentDetails(policyNumber, policyPaymentId)).withRel("paymentDetails"));
		dto.add(CommonLinks.branch(entity.getBranch()));
		String policyId = entity.getPolicy() != null? entity.getPolicy().getId(): null;
		dto.add(linkTo(methodOn(PolicyController.class).get(policyId)).withRel("policy"));
		return dto;
	}

}
