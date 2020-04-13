package lfs.server.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import lfs.server.core.AuditableRepository;
import lfs.server.core.BaseService;
import lfs.server.core.EntityWithId;
import lfs.server.exceptions.ObjectNotFoundException;

public abstract class ServiceUnitTest<T extends EntityWithId<ID>, ID> {
	
	private AuditableRepository<T, ID> repo;
	private BaseService<T, ID, AuditableRepository<T,ID>> service;
	
	protected T entity;
	protected List<T> list;
	
	@SuppressWarnings("unchecked")
	public ServiceUnitTest() {
		list = initializeList();
		entity = list.get(0);
		
		repo = mock(AuditableRepository.class);
		service = new BaseService<>(repo) {
			protected String className() {
				return entity.getClass().getSimpleName();
			};
		};
	}
	
	protected abstract List<T> initializeList();
	
	public boolean get() throws Exception {
		when(repo.findById(entity.getId())).thenReturn(Optional.of(entity));
		T response = service.get(entity.getId()).orElse(null);
		assertThat(response).isEqualTo(entity);
		return true;
	}
	
	public boolean all() {
		PageRequest pagable = PageRequest.of(0, 1);
		when(repo.findAll(pagable)).thenReturn(new PageImpl<T>(List.of(entity), pagable, 1));
		
		Page<T> page = service.all(pagable);
		assertThat(page).isNotEmpty();
		assertThat(page).hasSize(1);
		assertThat(page.get()).first().isEqualTo(entity);
		return true;
	}

	public boolean save() throws Exception {
		when(repo.save(any(getEntityType()))).thenReturn(entity);
		T response = service.save(newObject());
		assertThat(response)
			.isNotNull()
			.isEqualTo(response);
		return true;
	}
	
	public boolean update() throws Exception {
		var id = entity.getId();
		when(repo.existsById(id)).thenReturn(true);
		when(repo.save(any(getEntityType()))).thenReturn(entity);

		T response = service.update(id, newObject());
		assertThat(response)
			.isNotNull()
			.isEqualTo(response);
		return true;
	}
	
	public boolean failtWithUnknownId(ID unknownId) {
		when(repo.existsById(unknownId)).thenReturn(false);

		Throwable thrown = catchThrowable(() -> {
			service.update(unknownId, newObject());
		});
		assertThat(thrown).isInstanceOf(ObjectNotFoundException.class);
		assertThat(thrown).hasMessageContaining("not found");
		return true;
	}
	
	public boolean delete() {
		ID id = entity.getId();
		service.delete(id);
		verify(repo).deleteById(id);
		
		return true;
	}
	
	private T newObject() throws Exception {
		return getEntityType().getDeclaredConstructor().newInstance();
	}
	
	private Class<T> getEntityType() {
		ParameterizedType superClass = (ParameterizedType) getClass().getGenericSuperclass();
		@SuppressWarnings("unchecked")
		Class<T> type = (Class<T>) superClass.getActualTypeArguments()[0];
		return type;
	}
}
