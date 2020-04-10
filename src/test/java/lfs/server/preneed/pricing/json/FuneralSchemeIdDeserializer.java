package lfs.server.preneed.pricing.json;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import lfs.server.preneed.pricing.FuneralScheme;

public class FuneralSchemeIdDeserializer extends StdDeserializer<FuneralScheme> {
	private static final long serialVersionUID = 1L;

	FuneralSchemesJSON json = new FuneralSchemesJSON();
	protected FuneralSchemeIdDeserializer() {
		this(null);
	}
	
	protected FuneralSchemeIdDeserializer(Class<?> vc) {
		super(vc);
	}

	@Override
	public FuneralScheme deserialize(JsonParser jp, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		JsonNode node = jp.getCodec().readTree(jp);
		try {
			int id = node.asInt();
			return json.get(id);
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;
	} 

}
