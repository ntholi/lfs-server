package lfs.server.mortuary;

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
	public ResponseEntity<PagedModel<EntityModel<CorpseResponseDTO>>> all(Pageable pageable) {
		Page<Corpse> page = service.all(pageable);
		if(!page.isEmpty()) {
			return ResponseEntity.ok(pagedAssembler.toModel(page, assembler));
		}
		return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@PostMapping
	public ResponseEntity<EntityModel<CorpseResponseDTO>> save(@Valid @RequestBody Corpse corpse) {
		var model = assembler.toModel(service.save(corpse));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@PutMapping("/{tagNo}")
	public ResponseEntity<EntityModel<CorpseResponseDTO>> update(@PathVariable String tagNo, @RequestBody Corpse corpse) {
		var update = service.update(tagNo, corpse);
		return ResponseEntity.ok(assembler.toModel(update));
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
}
