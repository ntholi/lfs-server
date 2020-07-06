package com.breakoutms.lfs.server.mortuary.corpse;

import java.util.List;

import javax.validation.Valid;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseDTO;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseViewModel;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;
import com.breakoutms.lfs.server.mortuary.corpse.model.OtherMortuary;
import com.breakoutms.lfs.server.security.Domain;
import com.breakoutms.lfs.server.transport.Transport;
import com.breakoutms.lfs.server.transport.Vehicle;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.MORTUARY)
@AllArgsConstructor
public class CorpseController implements ViewModelController<Corpse, CorpseViewModel> {
	
	private final CorpseService service;
	private final PagedResourcesAssembler<CorpseViewModel> pagedAssembler;


	@GetMapping("/corpses/{id}")
	public ResponseEntity<CorpseViewModel> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Corpse", id));
	}

	@GetMapping("/corpses/") 
	public ResponseEntity<PagedModel<EntityModel<CorpseViewModel>>> all(Pageable pageable) {
		return ResponseHelper.pagedGetResponse(this, 
				pagedAssembler,
				service.all(pageable));
	}

	@PostMapping("/corpses/")
	public ResponseEntity<CorpseViewModel> save(@Valid @RequestBody CorpseDTO dto) {
		Corpse entity = map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/corpses/{id}")
	public ResponseEntity<CorpseViewModel> update(@PathVariable String id, 
			@Valid @RequestBody CorpseDTO dto) {
		Corpse entity = map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("/corpses/{tagNo}/next-of-kins/")
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
	public CorpseViewModel toViewModel(Corpse entity) {
		CorpseViewModel viewModel = CorpseMapper.INSTANCE.map(entity);
		val id = entity.getId();
		viewModel.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return viewModel;
	}
	
	private Corpse map(CorpseDTO dto) {
		Corpse entity = CorpseMapper.INSTANCE.map(dto);
		Transport transport = entity.getTransport();
		if (transport != null) {
			Vehicle vehicle = transport.getVehicle();
			if (vehicle != null && vehicle.getOwner() == null && vehicle.getRegistrationNumber() == null) {
				transport.setVehicle(null);
			}
			if (transport.getVehicle() == null && transport.getDriver() == null) {
				entity.setTransport(null);
			} 
		}
		return entity;
	}
}
