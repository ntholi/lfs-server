package com.breakoutms.lfs.server.preneed.pricing.utils;

import com.breakoutms.lfs.server.preneed.pricing.model.FuneralScheme;
import com.breakoutms.lfs.server.preneed.pricing.model.Premium;

public class FuneralSchemeUtils {

	public static Premium getPremium(FuneralScheme funeralScheme, int age) {
		return funeralScheme.getPremiums()
			.stream()
			.filter(fs -> age >= fs.getMinmumAge() && age <= fs.getMaximumAge())
			.findFirst()
			.get();
	}
}
