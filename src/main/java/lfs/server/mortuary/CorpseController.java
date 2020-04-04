package lfs.server.mortuary;

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

import lfs.server.exceptions.ExceptionSupplier;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/corpses")
@AllArgsConstructor
public class CorpseController  {

	private CorpseService service;
	private CorpseModelAssembler assembler;
	private PagedResourcesAssembler<Corpse> pagedAssembler;

	@GetMapping("/{id}")
	public ResponseEntity<EntityModel<CorpseResponseDTO>> get(@PathVariable String id) {
		return service.get(id)
				.map(o -> ResponseEntity.ok(assembler.toModel(o)))
				.orElseThrow(ExceptionSupplier.corpseNotFound(id));
	}

	@GetMapping
	public PagedModel<EntityModel<CorpseResponseDTO>> all(Pageable pageable) {
		return pagedAssembler.toModel(service.all(pageable), assembler);
	}

	@PostMapping
	public ResponseEntity<EntityModel<CorpseResponseDTO>> save(@Valid @RequestBody Corpse corpse) {
		var model = assembler.toModel(service.save(corpse));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@PutMapping("/{tagNo}")
	public Corpse update(@PathVariable String tagNo, @RequestBody Corpse corpse) {
		return service.update(tagNo, corpse);
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
	public Iterable<OtherMortuary> getOtherMortuaries() {
		return service.getOtherMortuaries();
	}

	@GetMapping("/next-of-kins/{tagNo}")
	public Iterable<NextOfKin> getNextOfKins(@PathVariable String tagNo) {
		return service.getNextOfKins(tagNo);
	}
}
