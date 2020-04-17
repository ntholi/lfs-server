package lfs.server.core;

import org.springframework.hateoas.RepresentationModel;

public interface EntityController <T extends Entity<?>, D extends RepresentationModel<? extends D>>{

	public D createDtoWithLinks(T entity);
}
