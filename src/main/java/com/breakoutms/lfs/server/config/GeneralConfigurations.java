package com.breakoutms.lfs.server.config;

import java.io.IOException;
import java.math.BigDecimal;

import org.apache.commons.lang3.StringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.deser.std.StringDeserializer;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;

@Configuration
public class GeneralConfigurations {

	@Bean
	@Primary
	public ObjectMapper objectMapper(Jackson2ObjectMapperBuilder builder) {
		ObjectMapper objectMapper = builder.createXmlMapper(false).build();
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		SimpleModule emptyStringModule = new SimpleModule();
		emptyStringModule.addDeserializer(String.class, new StdDeserializer<String>(String.class) {
			private static final long serialVersionUID = 1L;

			@Override
		    public String deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
		        String result = StringDeserializer.instance.deserialize(p, ctxt);
		        if (StringUtils.isEmpty(result)) {
		            return null;
		        }
		        return result;
		    }
		});

		objectMapper.registerModule(emptyStringModule);

		SimpleModule bigDecimalModule = new SimpleModule();
		bigDecimalModule.addSerializer(BigDecimal.class, new ToStringSerializer());
		objectMapper.registerModule(bigDecimalModule);


		return objectMapper;
	}
}
