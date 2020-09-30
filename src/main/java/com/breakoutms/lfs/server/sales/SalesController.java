package com.breakoutms.lfs.server.sales;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.time.LocalDateTime;

import javax.validation.Valid;

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
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.mortuary.corpse.CorpseController;
import com.breakoutms.lfs.server.sales.model.Quotation;
import com.breakoutms.lfs.server.sales.model.Sales;
import com.breakoutms.lfs.server.sales.model.SalesDTO;
import com.breakoutms.lfs.server.sales.model.SalesInquiry;
import com.breakoutms.lfs.server.sales.model.SalesProduct;
import com.breakoutms.lfs.server.sales.model.SalesProductDTO;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.SALES)
@AllArgsConstructor
public class SalesController implements ViewModelController<Sales, SalesDTO> {

	private final SalesService service;
	private final PagedResourcesAssembler<SalesDTO> pagedAssembler;

//	@GetMapping("/{id}")
//	public ResponseEntity<SalesEagerResponse> get(@PathVariable Integer id) {
//		return service.get(id)
//				.map(o -> ResponseEntity.ok(toEagerResponse(o)))
//				.orElse(ResponseEntity.noContent().build());
//	}
//	
//	private SalesEagerResponse toEagerResponse(Sales entity) {
//		SalesEagerResponse response = SalesMapper.INSTANCE.eager(entity);
//		return response;
//	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<SalesDTO>>> all(
			@SearchSpec Specification<Sales> specs, Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.search(specs, pageable));
	}

	@PostMapping
	public ResponseEntity<SalesDTO> save(@Valid @RequestBody SalesDTO dto) {
		Sales entity = SalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<SalesDTO> update(@PathVariable Integer id, 
			@Valid @RequestBody SalesDTO dto) {
		Sales entity = SalesMapper.INSTANCE.map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("inquire")
	public ResponseEntity<EntityModel<SalesInquiry>> inquire(String tagNo){
		SalesInquiry inquiry = service.salesInquiry(tagNo);
		if(inquiry == null) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(
				EntityModel.of(inquiry,
						linkTo(methodOn(CorpseController.class).get(tagNo)).withRel("corpse")));
	}
	
	@GetMapping("refrigeration-price")
	public SalesProductDTO refrigerationPrice(String tagNo, String leavingTime) {
		SalesProduct salesProduct = service.refrigerationPrice(tagNo, LocalDateTime.parse(leavingTime));
		return SalesMapper.INSTANCE.map(salesProduct);
	}
	
	@Override
	public SalesDTO toViewModel(Sales entity) {
		SalesDTO dto = SalesMapper.INSTANCE.map(entity);
		return addLinks(entity, dto);
	}

	private <T extends SalesDTO> T addLinks(Sales entity, T dto) {
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
