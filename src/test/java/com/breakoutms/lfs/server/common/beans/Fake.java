package com.breakoutms.lfs.server.common.beans;

import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class Fake<T> {

	private EasyRandomParameters parameters = new EasyRandomParameters()
			.seed(123L)
			.objectPoolSize(100)
			.randomizationDepth(3)
			.charset(Charset.forName("UTF-8"))
			.stringLengthRange(3, 8)
			.collectionSizeRange(1, 3)
			.scanClasspathForConcreteTypes(true)
			.overrideDefaultInitialization(false)
			.ignoreRandomizationErrors(true);

	private EasyRandom easyRandom;
	private Class<T> persistentClass;
	protected T entity;
	
	@SuppressWarnings("unchecked")
	public Fake() {
		easyRandom = new EasyRandom(parameters);
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
		entity = easyRandom.nextObject(persistentClass);
	}
	
	public T build() {
		return entity;
	}
	
}
