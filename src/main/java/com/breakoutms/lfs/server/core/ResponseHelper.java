package com.breakoutms.lfs.server.core;

import java.util.Optional;
import java.util.function.Supplier;

import javax.validation.constraints.NotNull;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseHelper {

	private ResponseHelper() {}

	public static <T extends Entity<?>,  D extends RepresentationModel<? extends D>> 
		ResponseEntity<D> getResponse(
			@NotNull final ViewModelController<T, D> controller,
			@NotNull final Optional<T> entityOptional, 
			@NotNull final Supplier<RuntimeException> notFoundExceptionSupplier) {
		return entityOptional
				.map(o -> ResponseEntity.ok(controller.toViewModel(o)))
				.orElseThrow(notFoundExceptionSupplier);
	}

	public static <T extends Entity<?>,  D extends RepresentationModel<? extends D>> 
		ResponseEntity<PagedModel<EntityModel<D>>> pagedGetResponse(
			@NotNull final ViewModelController<T, D> controller,
			@NotNull final PagedResourcesAssembler<D> assembler,
			@NotNull final Page<T> pagedList) {

		if(pagedList.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		Page<D> page = pagedList
				.map(entity -> controller.toViewModel(entity));
		return new ResponseEntity<>(assembler.toModel(page), HttpStatus.OK);
	}
}
