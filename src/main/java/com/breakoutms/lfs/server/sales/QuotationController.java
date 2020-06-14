package com.breakoutms.lfs.server.sales;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.CollectionModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.sales.model.SalesProductViewModel;
import com.breakoutms.lfs.server.security.Domain;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.SALES+"/quotations")
@AllArgsConstructor
public class QuotationController {

	private final SalesService service;
	
	@GetMapping("/{quotationNo}/sales-products")
	public ResponseEntity<CollectionModel<SalesProductViewModel>> getSalesProducts(@PathVariable Integer quotationNo){
		var list = SalesMapper.INSTANCE.map(service.getSalesProducts(quotationNo));
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		var result = CollectionModel.of(list, linkTo(methodOn(getClass())
				.getSalesProducts(quotationNo)).withSelfRel());
		
		return ResponseEntity.ok(result);
	}
}
