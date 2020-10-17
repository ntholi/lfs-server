package com.breakoutms.lfs.server.branch;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.branch.model.Branch;
import com.breakoutms.lfs.server.branch.model.BranchDTO;

@Mapper(componentModel="spring")
public abstract class BranchMapper {

	public static final BranchMapper INSTANCE = Mappers.getMapper(BranchMapper.class);
	
	@Mapping(target = "id", ignore = true)
	protected abstract void update(Branch updateEntity, @MappingTarget Branch saved);

	protected abstract Branch map(BranchDTO dto);

	protected abstract BranchDTO map(Branch entity);
}
