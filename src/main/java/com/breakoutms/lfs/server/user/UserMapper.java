package com.breakoutms.lfs.server.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.model.UserDTO;

@Mapper(componentModel="spring")
public abstract class UserMapper {

	public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	@Mapping(source = "branchName", target = "branch.name")
	protected abstract User map(UserDTO dto);

	@Mapping(source = "branch.name", target = "branchName")
	protected abstract UserDTO map(User entity);
	
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	protected abstract void update(User updateEntity, @MappingTarget User saved);
}
