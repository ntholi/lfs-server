package com.breakoutms.lfs.server.undertaker;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.breakoutms.lfs.common.enums.Domain;
import com.breakoutms.lfs.server.undertaker.model.UndertakerRequest;
import com.breakoutms.lfs.server.undertaker.model.UndertakerRequestInquiry;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/"+Domain.Const.UNDERTAKER+"/undertaker-requests")
@AllArgsConstructor
public class UndertakerRequestController {

	private final UndertakerRequestService service;
	private final PagedResourcesAssembler<UndertakerRequestInquiry> assembler;

	@GetMapping
	public ResponseEntity<PagedModel<EntityModel<UndertakerRequestInquiry>>> inquire(Pageable pageable) {
		Page<UndertakerRequest> page = service.all(pageable);
		if(page.isEmpty()) {
			return new ResponseEntity<>(HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(assembler.toModel(page.map(this::map)), HttpStatus.OK);
	}

	private UndertakerRequestInquiry map(UndertakerRequest uRequest) {
		UndertakerRequestInquiry inquiry = new UndertakerRequestInquiry();
		inquiry.setId(uRequest.getId());
		inquiry.setSeen(uRequest.isSeen());
		inquiry.setTagNo(uRequest.getCorpse().getTagNo());
		inquiry.setProcessed(uRequest.isProcessed());
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
		return inquiry;
	}

}
