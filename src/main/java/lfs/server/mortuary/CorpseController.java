package lfs.server.mortuary;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lfs.server.core.BaseController;
import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/corpses")
@AllArgsConstructor
public class CorpseController extends BaseController<Corpse, CorpseResponseDTO, String>  {

	private CorpseService service;
	private PagedResourcesAssembler<CorpseResponseDTO> pagedAssembler;

	@Override
	@GetMapping("/{id}")
	public ResponseEntity<CorpseResponseDTO> get(@PathVariable String id) {
		return getResponse(service.get(id), ExceptionSupplier.corpseNotFound(id));
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<CorpseResponseDTO>>> all(Pageable pageable) {
		Page<CorpseResponseDTO> page = service.all(pageable)
				.map(o -> addLinks(o));
		return page.isEmpty()? new ResponseEntity<>(HttpStatus.NO_CONTENT) 
				: new ResponseEntity<>(pagedAssembler.toModel(page),HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<CorpseResponseDTO> save(@Valid @RequestBody Corpse corpse) {
		var model = addLinks(service.save(corpse));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@PutMapping("/{tagNo}")
	public ResponseEntity<CorpseResponseDTO> update(@PathVariable String tagNo, @RequestBody Corpse corpse) {
		var update = service.update(tagNo, corpse);
		return ResponseEntity.ok(addLinks(update));
	}

	@DeleteMapping("/{tagNo}")
	public void delete(String tagNo) {
		service.delete(tagNo);
	}

	@GetMapping("other-mortuaries/{id}")
	public ResponseEntity<OtherMortuary> getTransforedFrom(@PathVariable Integer id) {
		return service.getTransforedFrom(id)
				.map(ResponseEntity::ok)
				.orElseThrow(ExceptionSupplier.notFound("OtherMortuary", id));
	}

	@GetMapping("/other-mortuaries")
	public ResponseEntity<Iterable<OtherMortuary>> getOtherMortuaries() {
		var list = service.getOtherMortuaries();
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}

	@GetMapping("/next-of-kins/{tagNo}")
	public ResponseEntity<List<NextOfKin>> getNextOfKins(@PathVariable String tagNo) {
		var list =  service.getNextOfKins(tagNo);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
	}

	@Override
	protected CorpseResponseDTO generateDTO(Corpse entity) {
		return CorpseMapper.INSTANCE.toDto(entity);
	}

	@Override
	protected CorpseResponseDTO addLinks(Corpse entity) {
		CorpseResponseDTO dto = super.addLinks(entity);
		dto.add(linkTo(methodOn(CorpseController.class).getNextOfKins(entity.getId())).withRel("nextOfKins"));
		OtherMortuary otherMortuaryId = entity.getTransferredFrom();
		if(otherMortuaryId != null) {
			dto.add(linkTo(methodOn(CorpseController.class).getTransforedFrom(otherMortuaryId.getId()))
					.withRel("transferredFrom"));
		}
		return dto;
	}
}
