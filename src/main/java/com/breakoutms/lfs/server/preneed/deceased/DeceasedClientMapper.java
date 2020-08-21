package com.breakoutms.lfs.server.preneed.deceased;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClient;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientDTO;
import com.breakoutms.lfs.server.preneed.deceased.model.DeceasedClientViewModel;

@Mapper(componentModel="spring")
public abstract class DeceasedClientMapper {
	
	public static final DeceasedClientMapper INSTANCE = Mappers.getMapper(DeceasedClientMapper.class);

	@Mapping(target = "id", ignore = true)
	@Mapping(target = "createdAt", ignore = true)
	@Mapping(target = "createdBy", ignore = true)
	@Mapping(target = "branch", ignore = true)
	@Mapping(target = "policy", ignore = true)
	public abstract void update(DeceasedClient updatedEntity, @MappingTarget DeceasedClient saved);
	
	public abstract DeceasedClient map(DeceasedClientDTO dto);
	
	@Mapping(source = "policy.policyNumber", target = "policyNumber")
	public abstract DeceasedClientViewModel map(DeceasedClient entity);
	
	public abstract DeceasedClientDTO toDTO(DeceasedClient entity);
}
