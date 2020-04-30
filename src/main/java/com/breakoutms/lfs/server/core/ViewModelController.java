package com.breakoutms.lfs.server.core;

import org.springframework.hateoas.RepresentationModel;

public interface ViewModelController <T extends Entity<?>, D extends RepresentationModel<? extends D>>{

	public D toViewModel(T entity);
}
