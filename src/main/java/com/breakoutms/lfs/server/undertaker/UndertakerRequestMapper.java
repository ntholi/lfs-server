package com.breakoutms.lfs.server.undertaker;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequest;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequestDTO;
import com.breakoutms.lfs.server.undertaker.postmortem.model.PostmortemRequestViewModel;

@Mapper(componentModel="spring")
public abstract class UndertakerRequestMapper {

	public static final UndertakerRequestMapper INSTANCE = Mappers.getMapper(UndertakerRequestMapper.class);
	
	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	
	public abstract void update(PostmortemRequest updateEntity, @MappingTarget PostmortemRequest saved);

	@Mapping(source = "tagNo", target = "corpse.tagNo")
	public
	abstract PostmortemRequest map(PostmortemRequestDTO dto);

	@Mapping(source = "corpse.tagNo", target = "tagNo")
	public
	abstract PostmortemRequestViewModel map(PostmortemRequest entity);
}