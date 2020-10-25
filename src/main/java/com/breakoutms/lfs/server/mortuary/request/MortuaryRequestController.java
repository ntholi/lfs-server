package com.breakoutms.lfs.server.mortuary.request;

import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.mortuary.corpse.model.Corpse;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequest;
import com.breakoutms.lfs.server.mortuary.request.model.MortuaryRequestInquiry;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;
import com.sipios.springsearch.anotation.SearchSpec;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.MORTUARY)
@AllArgsConstructor
public class MortuaryRequestController {

	private final MortuaryRequestService service;
	private final PagedResourcesAssembler<MortuaryRequestInquiry> assembler;

	@GetMapping("/requests")
	public ResponseEntity<PagedModel<EntityModel<MortuaryRequestInquiry>>> all(
			@SearchSpec Specification<MortuaryRequest> specs, Pageable pageable) {
		Page<MortuaryRequest> page = service.search(specs, pageable);
		if(page.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(assembler.toModel(page.map(this::map)), HttpStatus.OK);
	}
	
	@GetMapping("/requests-lookup")
	public ResponseEntity<CollectionModel<MortuaryRequestInquiry>> lookup(String names) {
		if(StringUtils.isBlank(names)) {
			return ResponseEntity.noContent()
					.build();
		}
		var list = service.lookup(names);
		if(list.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return ResponseEntity.ok(
				CollectionModel.of(
					list.stream()
						.map(this::map)
						.collect(Collectors.toList()))
		);
	}

	private MortuaryRequestInquiry map(MortuaryRequest uRequest) {
		MortuaryRequestInquiry inquiry = new MortuaryRequestInquiry();
		Corpse corpse = uRequest.getCorpse();
		inquiry.setId(uRequest.getId());
		inquiry.setSeen(uRequest.isSeen());
		inquiry.setTagNo(corpse.getTagNo());
		inquiry.setProcessed(uRequest.isProcessed());
		inquiry.setDate(uRequest.getCreatedAt());
		inquiry.setRequestType(uRequest.getRequestType());
		inquiry.setCorpseNames(uRequest.getCorpse().getFullName());
		if (uRequest instanceof PostmortemRequest) {
			PostmortemRequest request = (PostmortemRequest) uRequest;
			inquiry.setRequestedBy(request.getRequestedBy());
			inquiry.setRequestPerson(request.getRequestPerson());
			inquiry.setPhoneNumber(request.getPhoneNumber());
			inquiry.setLocation(request.getLocation());
		}
		else if (uRequest instanceof TransferRequest) {
			TransferRequest request = (TransferRequest) uRequest;
			inquiry.setTransferTo(request.getBranch().getName());

		}
//		else if(uRequest instanceof EmbalmingRequest) {
//			EmbalmingRequest request = (EmbalmingRequest) uRequest;
//		}
		return inquiry;
	}

}
