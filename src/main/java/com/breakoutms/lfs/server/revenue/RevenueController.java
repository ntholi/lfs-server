package com.breakoutms.lfs.server.revenue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDate;
import java.util.Map;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueDTO;
import com.breakoutms.lfs.server.revenue.model.RevenueEagerResponse;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.revenue.report.RevenueReportsService;
import com.breakoutms.lfs.server.sales.QuotationController;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.REVENUE)
@AllArgsConstructor
public class RevenueController implements ViewModelController<Revenue, RevenueDTO> {

	private final RevenueService service;
	private final RevenueReportsService reportsService;
	private final PagedResourcesAssembler<RevenueDTO> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<RevenueEagerResponse> get(@PathVariable Integer id) {
		return service.get(id)
				.map(o -> ResponseEntity.ok(toEagerResponse(o)))
				.orElse(ResponseEntity.noContent().build());
	}
	
	private RevenueEagerResponse toEagerResponse(Revenue entity) {
		RevenueEagerResponse response = RevenueMapper.INSTANCE.eager(entity);
		return addLinks(entity, response);
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<RevenueDTO>>> all(
			@SearchSpec Specification<Revenue> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}

	@PostMapping
	public ResponseEntity<RevenueDTO> save(@Valid @RequestBody RevenueDTO dto) {
		Revenue entity = RevenueMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RevenueDTO> update(@PathVariable Integer id, 
			@Valid @RequestBody RevenueDTO dto) {
		Revenue entity = RevenueMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("inquire")
	public ResponseEntity<EntityModel<RevenueInquiry>> inquire(Integer quotationNo){
		RevenueInquiry inquiry = service.revenueInquiry(quotationNo);
		if(inquiry == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(EntityModel.of(inquiry));
	}
	
	@GetMapping("/reports/revenue-report")
	public Map<String, Object> reports(int reportType, String from, String to, 
			@RequestParam(required = false) Integer branch, 
			@RequestParam(required = false) Integer user) {
		LocalDate fromDate = StringUtils.isNotBlank(from)? LocalDate.parse(from): null;
		LocalDate toDate = StringUtils.isNotBlank(to)? LocalDate.parse(to): null;
		Map<String, Object> res = null;
		if(reportType <= 0) {
			res = reportsService.getCollectionsReport(fromDate, toDate, branch, user);
		}
		else if(reportType == 1) {
			res = reportsService.getProductSummaryReport(fromDate, toDate, branch, user);
		}
		else {
			res = reportsService.getRevenueReport(fromDate, toDate, branch, user);
		}
		return res;
	}
	
	@Override
	public RevenueDTO toViewModel(Revenue entity) {
		RevenueDTO dto = RevenueMapper.INSTANCE.map(entity);
		return addLinks(entity, dto);
	}

	private <T extends RevenueDTO> T addLinks(Revenue entity, T dto) {
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		Quotation quotation = entity.getQuotation();
		if(quotation != null) {
			Integer quotNo = quotation.getId();
			dto.add(linkTo(methodOn(QuotationController.class).getSalesProducts(quotNo)).withRel("salesProducts"));
		}
		return dto;
	}
}
