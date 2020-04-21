package com.breakoutms.lfs.server.preneed.pricing.json;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import com.breakoutms.lfs.server.common.JSONReader;
import com.breakoutms.lfs.server.preneed.pricing.DependentBenefit;
import com.breakoutms.lfs.server.preneed.pricing.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.FuneralSchemeBenefit;
import com.breakoutms.lfs.server.preneed.pricing.PenaltyDeductible;
import com.breakoutms.lfs.server.preneed.pricing.Premium;

public class FuneralSchemesJSON {

	private static List<FuneralScheme> list;
	private static JSONReader<FuneralScheme> json;
	
	static {
		try {
			json = new JSONReader<>(FuneralScheme.class);
			list = json.read("FuneralScheme.json");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static List<FuneralScheme> all() {
		return list;
	}
	
	public static FuneralScheme any() {
		return all().get(1);
	}
	
	public static FuneralScheme withDependancies() throws IOException {
		FuneralScheme fs = any();
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
				.filter(item -> item.getId() == id)
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
		return getPemiums().stream()
			.filter(item -> item.getFuneralScheme().getId() == fs.getId())
			.collect(Collectors.toList());
	}
	
	public static List<DependentBenefit> getDependentBenefit() throws IOException {
		JSONReader<DependentBenefit> reader = new JSONReader<>(DependentBenefit.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<DependentBenefit> premiums = reader.read("DependentBenefit.json");
		return premiums;
	}
	
	public static List<DependentBenefit> getDependentBenefit(FuneralScheme fs) throws IOException {
		return getDependentBenefit().stream()
				.filter(item -> item.getFuneralScheme().getId() == fs.getId())
				.collect(Collectors.toList());
	}
	
	public static List<FuneralSchemeBenefit> getFuneralSchemeBenefit() throws IOException {
		JSONReader<FuneralSchemeBenefit> reader = new JSONReader<>(FuneralSchemeBenefit.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<FuneralSchemeBenefit> premiums = reader.read("FuneralSchemeBenefit.json");
		return premiums;
	}
	
	public static List<FuneralSchemeBenefit> getFuneralSchemeBenefit(FuneralScheme fs) throws IOException {
		return getFuneralSchemeBenefit().stream()
				.filter(item -> item.getFuneralScheme().getId() == fs.getId())
				.collect(Collectors.toList());
	}
	
	public static List<PenaltyDeductible> getPenaltyDeductable() throws IOException {
		JSONReader<PenaltyDeductible> reader = new JSONReader<>(PenaltyDeductible.class);
		reader.addMixIn(FuneralSchemeMixIn.class);
		List<PenaltyDeductible> premiums = reader.read("PenaltyDeductable.json");
		return premiums;
	}
	
	public static List<PenaltyDeductible> getPenaltyDeductable(FuneralScheme fs) throws IOException {
		return getPenaltyDeductable().stream()
				.filter(item -> item.getFuneralScheme().getId() == fs.getId())
				.collect(Collectors.toList());
	}

}
