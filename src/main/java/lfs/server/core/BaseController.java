package lfs.server.core;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.Optional;
import java.util.function.Supplier;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.http.ResponseEntity;

import lfs.server.audit.AuditableEntity;
import lfs.server.branch.Branch;

public abstract class BaseController<T extends AuditableEntity<ID>, D extends RepresentationModel<? extends D>, ID> {

	protected abstract ResponseEntity<D> get(ID id);
	
	protected abstract D generateDTO(T entity);

	@SuppressWarnings("unchecked")
	protected D addLinks(T entity) {
		D dto = generateDTO(entity);
		dto.add(linkTo(methodOn(getClass()).get(entity.getId())).withSelfRel());
		dto.add(linkTo(getClass()).withRel("all"));
		Branch branch = entity.getBranch();
		if(branch != null) {
			linkTo(getClass()).slash(branch.getId()).withRel("branch");
		}
		return dto;
	}

	protected ResponseEntity<D> getResponse(Optional<T> entityOptional, 
			Supplier<RuntimeException> exceptionSupplier) {
		return entityOptional
				.map(o -> okay(o))
				.orElseThrow(exceptionSupplier);
	}
	
	protected ResponseEntity<D> okay(T entity){
		return ResponseEntity.ok(addLinks((entity)));
	}
}
