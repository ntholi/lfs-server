package com.breakoutms.lfs.server.undertaker;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequest;
import com.breakoutms.lfs.server.reception.embalming.model.EmbalmingRequestDTO;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequestDTO;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequest;
import com.breakoutms.lfs.server.undertaker.transfer.model.TransferRequestDTO;

@Mapper(componentModel="spring")
public abstract class UndertakerRequestMapper {

	public static final UndertakerRequestMapper INSTANCE = Mappers.getMapper(UndertakerRequestMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	public abstract void update(PostmortemRequest updateEntity, @MappingTarget PostmortemRequest saved);
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	public abstract PostmortemRequest map(PostmortemRequestDTO dto);
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	public abstract PostmortemRequestDTO map(PostmortemRequest entity);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	public abstract void update(TransferRequest updateEntity, @MappingTarget TransferRequest saved);
	@Mapping(source = "tagNo", target = "corpse.tagNo")
	@Mapping(source = "transferTo", target = "transferTo.name")
	public abstract TransferRequest map(TransferRequestDTO dto);
	@Mapping(source = "corpse.tagNo", target = "tagNo")
	@Mapping(source = "transferTo.name", target = "transferTo")
	public abstract TransferRequestDTO map(TransferRequest entity);
}