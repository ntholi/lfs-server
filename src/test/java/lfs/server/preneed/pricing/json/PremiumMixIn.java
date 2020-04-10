package lfs.server.preneed.pricing.json;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class PremiumMixIn extends FuneralSchemeMixIn {
	
	@JsonProperty("premium") abstract double getPremiumAmount();
	
}