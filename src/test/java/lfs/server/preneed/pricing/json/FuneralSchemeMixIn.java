package lfs.server.preneed.pricing.json;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import lfs.server.common.MixIn;
import lfs.server.preneed.pricing.FuneralScheme;

public abstract class FuneralSchemeMixIn extends MixIn {
	
	@JsonDeserialize(using = FuneralSchemeIdDeserializer.class)
	abstract FuneralScheme getFuneralScheme();
}