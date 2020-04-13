package lfs.server.preneed.pricing;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lfs.server.core.BaseController;
import lfs.server.core.DtoMapper;
import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/funeral-schemes")
@AllArgsConstructor
public class FuneralSchemeController extends BaseController<FuneralScheme, FuneralSchemeDTO, Integer>{

	private FuneralSchemeService service;
	
	private PagedResourcesAssembler<FuneralSchemeDTO> pagedAssembler;
	
	@Override
	@GetMapping("/{id}")
	public ResponseEntity<FuneralSchemeDTO> get(@PathVariable Integer id) {
		return getResponse(service.get(id), 
				ExceptionSupplier.notFound(FuneralScheme.class, id));
	}
	
	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<FuneralSchemeDTO>>> all(Pageable pageable) {
		Page<FuneralSchemeDTO> page = service.all(pageable)
				.map(o -> addLinks(o));
		return page.isEmpty()? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
				: new ResponseEntity<>(pagedAssembler.toModel(page),HttpStatus.OK);
	}

	protected FuneralSchemeDTO generateDTO(FuneralScheme entity) {
		return DtoMapper.INSTANCE.map(entity);
	}
	
	@PostMapping
	public ResponseEntity<FuneralSchemeDTO> save(@Valid @RequestBody FuneralScheme entity) {
		var model = addLinks(service.save(entity));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}
}
