package lfs.server.mortuary;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel="spring")
public interface CorpseMapper {
	
	CorpseMapper INSTANCE = Mappers.getMapper(CorpseMapper.class);
	
	CorpseResponseDTO toDto(Corpse corpse);
}
