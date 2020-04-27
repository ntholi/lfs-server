package com.breakoutms.lfs.server.common.copy;

import com.breakoutms.lfs.server.preneed.model.Policy;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;
import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class DeepCopy {
	
	
	public static Policy copy(Policy object) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new FuneralSchemeExclusion())
				.create();
		return gson.fromJson(gson.toJson(object), Policy.class);
	}
	
	public static FuneralScheme copy(FuneralScheme object) {
		Gson gson = new GsonBuilder()
				.setExclusionStrategies(new FuneralSchemeExclusion())
				.registerTypeAdapterFactory(HibernateProxyTypeAdapter.FACTORY)
				.create();
		return gson.fromJson(gson.toJson(object), FuneralScheme.class);
	}
	
	public static <T> T copy(T object, Class<T> type) {
		Gson gson = new GsonBuilder().create();
		return gson.fromJson(gson.toJson(object), type);
	}
	
	private static class FuneralSchemeExclusion implements ExclusionStrategy {
		@Override
		public boolean shouldSkipClass(Class<?> arg0) {
			return false;
		}
		@Override
		public boolean shouldSkipField(FieldAttributes f) {
			return (f.getDeclaringClass() == Premium.class && f.getName().equals("funeralScheme"))
					|| (f.getDeclaringClass() == DependentBenefit.class && f.getName().equals("funeralScheme"))
					|| (f.getDeclaringClass() == FuneralSchemeBenefit.class && f.getName().equals("funeralScheme"))
					|| (f.getDeclaringClass() == PenaltyDeductible.class && f.getName().equals("funeralScheme"));
		}
	}
}
