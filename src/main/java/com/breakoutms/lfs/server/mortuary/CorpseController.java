package com.breakoutms.lfs.server.mortuary;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.util.List;

import javax.validation.Valid;

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

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.DtoMapper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/corpses")
@AllArgsConstructor
class CorpseController implements ViewModelController<Corpse, CorpseResponseDTO>  {

	private final CorpseService service;
	private final PagedResourcesAssembler<CorpseResponseDTO> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<CorpseResponseDTO> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Funeral Scheme", id));
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<CorpseResponseDTO>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping
	public ResponseEntity<CorpseResponseDTO> save(@Valid @RequestBody Corpse entity) {
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}

	@PutMapping("/{tagNo}")
	public ResponseEntity<CorpseResponseDTO> update(@PathVariable String tagNo, @Valid @RequestBody Corpse corpse) {
		return ResponseEntity.ok(
				toViewModel(service.update(tagNo, corpse))
		);
	}

	@DeleteMapping("/{tagNo}")
	public void delete(String tagNo) {
		service.delete(tagNo);
	}

	@GetMapping("{tagNo}/next-of-kins/")
	public ResponseEntity<List<NextOfKin>> getNextOfKins(@PathVariable String tagNo) {
		var list =  service.getNextOfKins(tagNo);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(list);
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

	@Override
	public CorpseResponseDTO toViewModel(Corpse entity) {
		CorpseResponseDTO dto = DtoMapper.INSTANCE.map(entity);
		var id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		dto.add(linkTo(methodOn(getClass()).getNextOfKins(id)).withRel("nextOfKins"));
		var omId = entity.getTransferredFrom() != null? entity.getTransferredFrom().getId() : null;
		dto.add(linkTo(methodOn(CorpseController.class).getTransforedFrom(omId)).withRel("transferredFrom"));
		return dto;
	}
}
