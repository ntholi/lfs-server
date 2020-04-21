package com.breakoutms.lfs.server.preneed.pricing.json;

import com.breakoutms.lfs.server.common.MixIn;
import com.breakoutms.lfs.server.preneed.pricing.FuneralScheme;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public abstract class FuneralSchemeMixIn extends MixIn {
	
	@JsonDeserialize(using = FuneralSchemeIdDeserializer.class)
	abstract FuneralScheme getFuneralScheme();
}