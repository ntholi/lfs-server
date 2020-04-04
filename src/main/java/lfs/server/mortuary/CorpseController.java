package lfs.server.mortuary;

import javax.validation.Valid;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lfs.server.branch.Branch;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/corpses")
@AllArgsConstructor
public class CorpseController  {

	private CorpseService service;
	private CorpseModelAssembler assembler;
	private PagedResourcesAssembler<Corpse> pagedAssembler;

	@GetMapping("/{tagNo}")
	public ResponseEntity<EntityModel<CorpseResponseDTO>> get(@PathVariable String tagNo) {
		var corpse = service.get(tagNo);
		if(corpse.isPresent()) {
			var model = assembler.toModel(corpse.get());
			return new ResponseEntity<>(model, HttpStatus.OK);	
		}
		else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<CorpseResponseDTO>>> all(
			@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "20") Integer pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy) {

		var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
		var pageModel = pagedAssembler.toModel(service.all(pageable), assembler);

		return new ResponseEntity<>(pageModel, HttpStatus.OK);
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

	@GetMapping("/{tagNo}/branch")
	public Branch getBranch(String tagNo) {
		return null;
	}

	@GetMapping("other-mortuaries/{id}")
	public ResponseEntity<OtherMortuary> getTransforedFrom(@PathVariable Integer id) {
		var item = service.getTransforedFrom(id);
		if(item.isPresent()) {
			return new ResponseEntity<>(item.get(), HttpStatus.OK);	
		}
		else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
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
