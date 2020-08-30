package com.breakoutms.lfs.server.revenue;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

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

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.revenue.model.Revenue;
import com.breakoutms.lfs.server.revenue.model.RevenueDTO;
import com.breakoutms.lfs.server.revenue.model.RevenueEagerResponse;
import com.breakoutms.lfs.server.revenue.model.RevenueInquiry;
import com.breakoutms.lfs.server.revenue.model.RevenueViewModel;
import com.breakoutms.lfs.server.sales.QuotationController;
import com.breakoutms.lfs.server.sales.model.Quotation;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.REVENUE)
@AllArgsConstructor
public class RevenueController implements ViewModelController<Revenue, RevenueViewModel> {

	private final RevenueService service;
	private final PagedResourcesAssembler<RevenueViewModel> pagedAssembler;

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
	public ResponseEntity<PagedModel<EntityModel<RevenueViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<RevenueViewModel> save(@Valid @RequestBody RevenueDTO dto) {
		Revenue entity = RevenueMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<RevenueViewModel> update(@PathVariable Integer id, 
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
	
	@Override
	public RevenueViewModel toViewModel(Revenue entity) {
		RevenueViewModel dto = RevenueMapper.INSTANCE.map(entity);
		return addLinks(entity, dto);
	}

	private <T extends RevenueViewModel> T addLinks(Revenue entity, T dto) {
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
