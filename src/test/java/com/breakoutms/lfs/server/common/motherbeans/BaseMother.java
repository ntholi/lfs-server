package com.breakoutms.lfs.server.common.motherbeans;

import static org.jeasy.random.FieldPredicates.named;

import java.lang.reflect.ParameterizedType;
import java.math.BigDecimal;
import java.nio.charset.Charset;

import org.jeasy.random.EasyRandom;
import org.jeasy.random.EasyRandomParameters;

public class BaseMother<T> {

	private EasyRandom easyRandom;
	private Class<T> persistentClass;
	protected T entity;

	@SuppressWarnings("unchecked")
	public BaseMother() {
		EasyRandomParameters parameters = getRandomParameters();
		
		easyRandom = new EasyRandom(parameters);
		this.persistentClass = (Class<T>) ((ParameterizedType) getClass()
				.getGenericSuperclass()).getActualTypeArguments()[0];
		entity = easyRandom.nextObject(persistentClass);
	}

	protected EasyRandomParameters getRandomParameters() {
		long seed = 123L;//new Date().getTime();
		
		//TODO: SET INTEGER BOUNDS AND GENERATE BIG DECIMAL BASED ON validation annotations
		EasyRandomParameters parameters = new EasyRandomParameters()
				.seed(seed)
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
		return parameters;
	}
	
	public T build() {
		return entity;
	}
	
	public BigDecimal money(int amount) {
		return new BigDecimal(String.valueOf(amount)+".0");
	}

}
