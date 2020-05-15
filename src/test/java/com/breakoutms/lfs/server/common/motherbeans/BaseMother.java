package com.breakoutms.lfs.server.common.motherbeans;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;
import static org.jeasy.random.FieldPredicates.*;
import org.jeasy.random.api.Randomizer;

public class BaseMother<T> {

	private EasyRandom easyRandom;
	private Class<T> persistentClass;
	protected T entity;

	@SuppressWarnings("unchecked")
	public BaseMother() {
		EasyRandomParameters parameters = new EasyRandomParameters()
				.seed(123L)
				.objectPoolSize(100)
				.randomizationDepth(3)
				.charset(Charset.forName("UTF-8"))
				.stringLengthRange(3, 8)
				.collectionSizeRange(3, 3)
				.scanClasspathForConcreteTypes(true)
				.overrideDefaultInitialization(false)
				.ignoreRandomizationErrors(true)
				.excludeField(named("branch")
						.or(named("createdAt"))
						.or(named("createdBy"))
				)
				.randomize(named("deleted"), () -> false)
				.randomize(BigDecimal.class, () -> new BigDecimal("80.20"));
		
		easyRandom = new EasyRandom(parameters);
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		entity = easyRandom.nextObject(persistentClass);
	}
	
	public T build() {
		return entity;
	}

}
