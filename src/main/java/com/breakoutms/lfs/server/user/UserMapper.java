package com.breakoutms.lfs.server.user;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.user.model.User;
import com.breakoutms.lfs.server.user.model.UserDTO;

@Mapper(componentModel="spring")
public abstract class UserMapper {

	public static final UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
	
	public abstract User map(UserDTO userDto);
}
