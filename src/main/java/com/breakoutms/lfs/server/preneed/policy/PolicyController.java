package com.breakoutms.lfs.server.preneed.policy;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Import;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.config.GeneralConfigurations;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyViewModel;
import com.breakoutms.lfs.server.preneed.policy.report.PolicyReportsService;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeController;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED+"/policies")
@Import(GeneralConfigurations.class)
@AllArgsConstructor
public class PolicyController implements ViewModelController<Policy, PolicyViewModel> {

	private final PolicyService service;
	private final PolicyReportsService reportsService;
	private final PagedResourcesAssembler<PolicyViewModel> pagedAssembler;
	
	@GetMapping("/{id}")
	public ResponseEntity<PolicyViewModel> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Policy", id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<PolicyViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}
	
	@PostMapping
	public ResponseEntity<PolicyViewModel> save(@Valid @RequestBody PolicyDTO dto) {
		val entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity, dto.getFuneralScheme())), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<PolicyViewModel> update(@PathVariable String id, 
			@Valid @RequestBody PolicyDTO dto) {
		val entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity, dto.getFuneralScheme())), 
				HttpStatus.OK
		);
	}

	
	@GetMapping("/reports/policy-report")
	public Map<String, Object> reports(int reportType, String from, String to, 
			@RequestParam(required = false) Integer branch, 
			@RequestParam(required = false) Integer user) {
		LocalDate fromDate = StringUtils.isNotBlank(from)? LocalDate.parse(from): null;
		LocalDate toDate = StringUtils.isNotBlank(to)? LocalDate.parse(to): null;
		Map<String, Object> res = null;
		if(reportType <= 0) {
			res = reportsService.getPolicyReport(fromDate, toDate, branch, user);
		}
//		else {
//			res = reportsService.getRevenueReport(fromDate, toDate, branch, user);
//		}
		return res;
	}
	
	@Override
	public PolicyViewModel toViewModel(Policy entity) {
		var dto = PreneedMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		if(entity.getFuneralScheme() != null) {
			val funeralSchemId = entity.getFuneralScheme().getId();
			dto.add(linkTo(FuneralSchemeController.class).slash(funeralSchemId).withRel("funeralScheme"));	
		}
		return dto;
	}

}
