package com.breakoutms.lfs.server.products;

import javax.validation.Validator;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.MethodArgumentNotValidException;
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
import com.breakoutms.lfs.server.products.model.Product;
import com.breakoutms.lfs.server.products.model.ProductDTO;
import com.breakoutms.lfs.server.products.model.ProductViewModel;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.PRODUCTS)
@AllArgsConstructor
public class ProductController implements ViewModelController<Product, ProductViewModel> {

	private final Validator validator;
	private final ProductService service;
	private final PagedResourcesAssembler<ProductViewModel> pagedAssembler;


	@GetMapping("/{id}")
	public ResponseEntity<ProductViewModel> get(@PathVariable Integer id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Product", id));
	}

	@GetMapping 
	public ResponseEntity<PagedModel<EntityModel<ProductViewModel>>> all(String productType, 
			Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(productType, pageable));
	}

	//@Valid has been omitted here but used manual validation because ProductDTO
	//can be mapped into any subclass of Product where each subclass has it's own
	//validation requirements
	@PostMapping
	public ResponseEntity<ProductViewModel> save(@RequestBody ProductDTO dto) throws MethodArgumentNotValidException {
		Product entity = ProductFactory.get(dto);
		validate("save", entity);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}

	//@Valid has been omitted here but used manual validation
	@PutMapping("/{id}")
	public ResponseEntity<ProductViewModel> update(@PathVariable Integer id, 
			@RequestBody ProductDTO dto) throws MethodArgumentNotValidException {
		Product entity = ProductFactory.get(dto);
		validate("update", entity);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}

	protected void validate(String method, Product entity) throws MethodArgumentNotValidException {
	    SpringValidatorAdapter v = new SpringValidatorAdapter(validator);
	    BeanPropertyBindingResult errors = new BeanPropertyBindingResult(entity, Product.class.getSimpleName());
	    v.validate(entity, errors);
	    if (errors.hasErrors()) {
				throw new MethodArgumentNotValidException(null, errors);
	    }
	}

	@Override
	public ProductViewModel toViewModel(Product entity) {
		ProductViewModel viewModel = ProductFactory.get(entity);
		val id = entity.getId();
		viewModel.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return viewModel;
	}
}
