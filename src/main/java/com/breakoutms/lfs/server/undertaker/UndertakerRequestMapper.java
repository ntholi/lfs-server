package com.breakoutms.lfs.server.undertaker;

import javax.validation.Valid;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.undertaker.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.model.PostmortemRequestDTO;
import com.breakoutms.lfs.server.undertaker.model.PostmortemRequestViewModel;

@Mapper(componentModel="spring")
public abstract class UndertakerRequestMapper {

	public static final UndertakerRequestMapper INSTANCE = Mappers.getMapper(UndertakerRequestMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(PostmortemRequest updateEntity, @MappingTarget PostmortemRequest saved);

	@Mapping(source = "tagNo", target = "corpse.tagNo")
	protected abstract PostmortemRequest map(PostmortemRequestDTO dto);

	@Mapping(source = "corpse.tagNo", target = "tagNo")
	protected abstract PostmortemRequestViewModel map(PostmortemRequest entity);
}