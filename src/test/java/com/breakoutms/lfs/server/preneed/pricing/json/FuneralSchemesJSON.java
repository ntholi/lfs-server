package com.breakoutms.lfs.server.preneed.pricing.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import com.breakoutms.lfs.server.common.JSONReader;
import com.breakoutms.lfs.server.preneed.pricing.model.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.model.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

public class FuneralSchemesJSON {

	private List<FuneralScheme> list;
	private JSONReader<FuneralScheme> json;
	
	public FuneralSchemesJSON() {
		try {
			json = new JSONReader<>(FuneralScheme.class);
			list = json.read("FuneralScheme.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<FuneralScheme> all() {
		return new FuneralSchemesJSON().list;
	}
	
	public static FuneralScheme any() {
		var list = all();
		return list.get(ThreadLocalRandom.current().nextInt(list.size()));
	}
	
	public static FuneralScheme byName(String name) throws IOException {
		var fs = all().stream()
					.filter(i -> i.getName().equals(name))
					.findFirst()
					.get();
		return addDependancies(fs);
	}
	
	public static List<FuneralScheme> allWithDependancies() throws IOException {
		List<FuneralScheme> list = new ArrayList<>();
		for (FuneralScheme it : all()) {
			list.add(addDependancies(it));
		}
		return list;
	}
	
	public static FuneralScheme withDependancies() throws IOException {
		return addDependancies(any());
	}

	protected static FuneralScheme addDependancies(FuneralScheme fs) throws IOException {
		var benefits = getFuneralSchemeBenefit(fs);
		benefits.forEach(i -> i.setFuneralScheme(null));
		var premiums = getPemiums(fs);
		premiums.forEach(i -> i.setFuneralScheme(null));
		var dependentBen = getDependentBenefit(fs);
		dependentBen.forEach(i -> i.setFuneralScheme(null));
		var deductables = getPenaltyDeductable(fs);
		deductables.forEach(i -> i.setFuneralScheme(null));
		fs.setBenefits(benefits);
		fs.setPremiums(premiums);
		fs.setDependentBenefits(dependentBen);
		fs.setPenaltyDeductibles(deductables);
		return fs;
	}
	
	public static FuneralScheme withDependanciesButNoIds() throws IOException {
		FuneralScheme fs = any();
		var benefits = getFuneralSchemeBenefit(fs);
		benefits.forEach(i -> {
			i.setId(null);
			i.setFuneralScheme(null);
		});
		var premiums = getPemiums(fs);
		premiums.forEach(i -> {
			i.setId(null);
			i.setFuneralScheme(null);
		});
		var dependentBen = getDependentBenefit(fs);
		dependentBen.forEach(i -> {
			i.setId(null);
			i.setFuneralScheme(null);
		});
		var deductables = getPenaltyDeductable(fs);
		deductables.forEach(i -> {
			i.setId(null);
			i.setFuneralScheme(null);
		});
		fs.setId(null);
		fs.setBenefits(benefits);
		fs.setPremiums(premiums);
		fs.setDependentBenefits(dependentBen);
		fs.setPenaltyDeductibles(deductables);
		return fs;
	}
	
	public FuneralScheme get(int id) {
		return list.stream()
				.filter(item -> item.getId() != null)
				.filter(item -> item.getId().equals(id))
				.findFirst()
				.get();
	}

	public static List<Premium> getPemiums() throws IOException {
		JSONReader<Premium> reader = new JSONReader<>(Premium.class);
		reader.addMixIn(PremiumMixIn.class);
		List<Premium> premiums = reader.read("Premium.json");
		return premiums;
	}
	
	public static List<Premium> getPemiums(FuneralScheme fs) throws IOException {
		List<Premium> list = new ArrayList<>();
		for (var item : getPemiums()) {
			if(item.getFuneralScheme() != null 
					&& item.getFuneralScheme().getId().equals(fs.getId())) {
				list.add(item);
			}
		}
		return list;
	}
	
	public static List<DependentBenefit> getDependentBenefit() throws IOException {
		JSONReader<DependentBenefit> reader = new JSONReader<>(DependentBenefit.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<DependentBenefit> premiums = reader.read("DependentBenefit.json");
		return premiums;
	}
	
	public static List<DependentBenefit> getDependentBenefit(FuneralScheme fs) throws IOException {
		List<DependentBenefit> list = new ArrayList<>();
		for (var item : getDependentBenefit()) {
			if(item.getFuneralScheme() != null 
					&& item.getFuneralScheme().getId().equals(fs.getId())) {
				list.add(item);
			}
		}
		return list;
	}
	
	public static List<FuneralSchemeBenefit> getFuneralSchemeBenefit() throws IOException {
		JSONReader<FuneralSchemeBenefit> reader = new JSONReader<>(FuneralSchemeBenefit.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<FuneralSchemeBenefit> premiums = reader.read("FuneralSchemeBenefit.json");
		return premiums;
	}
	
	public static List<FuneralSchemeBenefit> getFuneralSchemeBenefit(FuneralScheme fs) throws IOException {
		List<FuneralSchemeBenefit> list = new ArrayList<>();
		for (var item : getFuneralSchemeBenefit()) {
			if(item.getFuneralScheme() != null 
					&& item.getFuneralScheme().getId().equals(fs.getId())) {
				list.add(item);
			}
		}
		return list;
	}
	
	public static List<PenaltyDeductible> getPenaltyDeductable() throws IOException {
		JSONReader<PenaltyDeductible> reader = new JSONReader<>(PenaltyDeductible.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<PenaltyDeductible> premiums = reader.read("PenaltyDeductable.json");
		return premiums;
	}
	
	public static List<PenaltyDeductible> getPenaltyDeductable(FuneralScheme fs) throws IOException {
		List<PenaltyDeductible> list = new ArrayList<>();
		for (var item : getPenaltyDeductable()) {
			if(item.getFuneralScheme() != null 
					&& item.getFuneralScheme().getId().equals(fs.getId())) {
				list.add(item);
			}
		}
		return list;
	}

}
