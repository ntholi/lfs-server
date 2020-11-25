package com.breakoutms.lfs.server.mortuary.corpse;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.core.CommonLinks;
import com.breakoutms.lfs.server.core.ResponseHelper;
import com.breakoutms.lfs.server.core.ViewModelController;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseDTO;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseProjection;
import com.breakoutms.lfs.server.mortuary.corpse.model.CorpseLookupProjection;
import com.breakoutms.lfs.server.mortuary.corpse.model.NextOfKin;
import com.breakoutms.lfs.server.mortuary.corpse.model.OtherMortuary;
import com.breakoutms.lfs.server.mortuary.corpse.report.CorpseReportService;
import com.breakoutms.lfs.server.transport.Transport;
import com.breakoutms.lfs.server.transport.Vehicle;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;
import lombok.val;

@RestController
@RequestMapping("/"+Domain.Const.MORTUARY)
@AllArgsConstructor
public class CorpseController implements ViewModelController<Corpse, CorpseDTO> {
	
	private final CorpseService service;
	private final CorpseReportService reportService;
	private final PagedResourcesAssembler<CorpseProjection> pagedAssembler;


	@GetMapping("/corpses/{id}")
	public ResponseEntity<CorpseDTO> get(@PathVariable String id) {
		return ResponseHelper.getResponse(this, 
				service.get(id), 
				ExceptionSupplier.notFound("Corpse", id));
	}

	@GetMapping("/corpses") 
	public ResponseEntity<PagedModel<EntityModel<CorpseProjection>>> all(
			@SearchSpec Specification<Corpse> specs, Pageable pageable) {
		var response = pagedAssembler.toModel(service.search(specs, pageable));
		return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/corpses")
	public ResponseEntity<CorpseDTO> save(@Valid @RequestBody CorpseDTO dto) {
		Corpse entity = map(dto);
		return new ResponseEntity<>(
				toViewModel(service.save(entity)), 
				HttpStatus.CREATED
		);
	}
	
	@PutMapping("/corpses/{id}")
	public ResponseEntity<CorpseDTO> update(@PathVariable String id, 
			@Valid @RequestBody CorpseDTO dto) {
		Corpse entity = map(dto);
		return new ResponseEntity<>(
				toViewModel(service.update(id, entity)), 
				HttpStatus.OK
		);
	}
	
	@GetMapping("/corpses/{tagNo}/next-of-kins")
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
	
	@GetMapping("/corpses-lookup")
	public ResponseEntity<CollectionModel<CorpseLookupProjection>> lookup(String names) {
		if(StringUtils.isBlank(names)) {
			return ResponseEntity.noContent()
					.build();
		}
		//TODO: IT HAS TO BE PAGABLE
//		return ResponseHelper.pagedGetResponse(this, 
//				pagedAssembler,
//				service.lookup(pageable));
		
		var list = service.lookup(names);
		return list.isEmpty()? 
				new ResponseEntity<>(HttpStatus.NO_CONTENT) : 
					ResponseEntity.ok(CollectionModel.of(list));
	}
	
	@GetMapping("/corpses/reports/corpse-report")
	public Map<String, Object> reports(int reportType, String from, String to, 
			@RequestParam(required = false) Integer branch, 
			@RequestParam(required = false) Integer user,
			@RequestParam(required = false) String tagNo) {
		LocalDate fromDate = StringUtils.isNotBlank(from)? LocalDate.parse(from): null;
		LocalDate toDate = StringUtils.isNotBlank(to)? LocalDate.parse(to): null;
		
		Map<String, Object> res = null;
		if(reportType <= 0) {
			res = reportService.getCorpseReport(fromDate, toDate, branch, user);
		}
		else {
			res = reportService.getDetailedCorpseReport(fromDate, toDate, branch, user, tagNo);
		}
		return res; 
	}
	
	
	@GetMapping("/corpses/autocomplete-data")
	public Map<String, List<String>> dataForAutocomplete() {
		Map<String, List<String>> data = new HashMap<>();
		var otherMotuaries = service.getOtherMortuaries()
				.stream()
				.map(OtherMortuary::getName)
				.collect(Collectors.toList());
		data.put("otherMotuaries", otherMotuaries);
		
		return data;
	}
	
	@Override
	public CorpseDTO toViewModel(Corpse entity) {
		var dto = CorpseMapper.INSTANCE.map(entity);
		val id = entity.getId();
		dto.add(CommonLinks.addLinksWithBranch(getClass(), id, entity.getBranch()));
		return dto;
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
