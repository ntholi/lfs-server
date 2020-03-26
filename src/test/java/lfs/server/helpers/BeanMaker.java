package lfs.server.helpers;

import java.util.ArrayList;
import java.util.List;

public abstract class BeanMaker<T> {

	private List<T> list = new ArrayList<>(); 
	
	BeanMaker() {
		list.addAll(createBeans());
	}
	
	protected abstract List<? extends T> createBeans();

	public T get(int index) {
		int size = list.size();
		if(index >= size) {
			index = size-1;
		} else if (index < 0) index = 0;
		return list.get(index);
	}
	
	public T getAny() {
		int index = 0;
		return get(index);
	}
}
