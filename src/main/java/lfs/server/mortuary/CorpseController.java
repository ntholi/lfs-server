package lfs.server.mortuary;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import java.io.Serializable;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
@RequestMapping("/corpses")
public class CorpseController  {

	private CorpseService service;
	private CorpseModelAssembler assembler;
	private PagedResourcesAssembler<Corpse> pagedAssembler;

	@Autowired
	public CorpseController(CorpseService service, CorpseModelAssembler assember, PagedResourcesAssembler<Corpse> pagedAssembler) {
		this.service = service;
		this.assembler = assember;
		this.pagedAssembler = pagedAssembler;
	}

	@GetMapping("/{tagNo}")
	public ResponseEntity<EntityModel<Corpse>> get(@PathVariable Serializable tagNo) {
		var model = assembler.toModel(service.get((String)tagNo));
		return new ResponseEntity<>(model, HttpStatus.OK);
	}

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<Corpse>>> all(@RequestParam(defaultValue = "0") Integer pageNo, 
			@RequestParam(defaultValue = "20") Integer pageSize,
			@RequestParam(defaultValue = "createdAt") String sortBy) {
		var pageable = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        var pageModel = pagedAssembler.toModel(service.all(pageable), assembler);
        pageModel.add(linkTo(methodOn(CorpseController.class).all(pageNo, pageSize, sortBy)).withSelfRel());
        return new ResponseEntity<>(pageModel, HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<EntityModel<Corpse>> save(@Valid @RequestBody Corpse corpse) {
		var model = assembler.toModel(service.save(corpse));
		return new ResponseEntity<>(model, HttpStatus.CREATED);
	}

	@PutMapping("/{tagNo}")
	public Corpse update(@PathVariable String tagNo, @RequestBody Corpse corpse) {
		return service.update(tagNo, corpse);
	}


	@GetMapping("/{tagNo}/transferred-from")
	public OtherMortuary getTransforedFrom(@PathVariable String tagNo) {
		return service.getOtherMortuaries(tagNo).iterator().next();
	}

	@GetMapping("/other-mortuaries")
	public Iterable<OtherMortuary> getOtherMortuaries() {
		return service.getOtherMortuaries();
	}

	@DeleteMapping("/{tagNo}")
	public void delete(String tagNo) {
		service.delete(tagNo);
	}
}
