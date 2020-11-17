package com.breakoutms.lfs.server.preneed.policy;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Import;
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
import com.breakoutms.lfs.server.config.ObjectMapperConfig;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.preneed.PreneedMapper;
import com.breakoutms.lfs.server.preneed.policy.model.Policy;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyDTO;
import com.breakoutms.lfs.server.preneed.policy.model.PolicyLookupProjection;
import com.breakoutms.lfs.server.preneed.policy.report.PolicyReportsService;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeController;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRENEED)
@Import(ObjectMapperConfig.class)
@AllArgsConstructor
public class PolicyController implements ViewModelController<Policy, PolicyDTO> {

	private final PolicyService service;
	private final PolicyReportsService reportsService;
	private final PagedResourcesAssembler<PolicyDTO> pagedAssembler;
	
	@GetMapping("/policies/{id}")
	public ResponseEntity<PolicyDTO> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Policy", id));
	}
	
	@GetMapping("/policies")
	public ResponseEntity<PagedModel<EntityModel<PolicyDTO>>> all(
			@SearchSpec Specification<Policy> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}
	
	@PostMapping("/policies")
	public ResponseEntity<PolicyDTO> save(@Valid @RequestBody PolicyDTO dto) {
		val entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity, dto.getFuneralScheme())), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/policies/{id}")
	public ResponseEntity<PolicyDTO> update(@PathVariable String id, 
			@Valid @RequestBody PolicyDTO dto) {
		val entity = PreneedMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity, dto.getFuneralScheme())), 
				HttpStatus.OK
		);
	}

	@GetMapping("/policies-lookup")
	public ResponseEntity<CollectionModel<PolicyLookupProjection>> lookup(String names) {
		if(StringUtils.isBlank(names)) {
			return ResponseEntity.noContent()
					.build();
		}
		//TODO: IT HAS TO BE PAGABLE
//		return ResponseHelper.pagedGetResponse(this, 
//				pagedAssembler,
//				service.lookup(pageable));
		
		var list = service.lookup(names);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(CollectionModel.of(list));
	}
	
	@GetMapping("/policies/reports/policy-report")
	public Map<String, Object> reports(int reportType, String from, String to, 
			@RequestParam(required = false) Integer branch, 
			@RequestParam(required = false) Integer user) {
		LocalDate fromDate = StringUtils.isNotBlank(from)? LocalDate.parse(from): null;
		LocalDate toDate = StringUtils.isNotBlank(to)? LocalDate.parse(to): null;
		Map<String, Object> res = null;
		if(reportType <= 0) {
			res = reportsService.getPolicyReport(fromDate, toDate, branch, user);
		}
		else if(reportType == 1){
			res = reportsService.getPlanTypeSummaryReport(fromDate, toDate, branch, user);
		}
		return res;
	}
	
	@Override
	public PolicyDTO toViewModel(Policy entity) {
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
