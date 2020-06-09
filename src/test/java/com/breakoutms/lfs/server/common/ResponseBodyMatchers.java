package com.breakoutms.lfs.server.common;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;

import com.breakoutms.lfs.server.exceptions.CentralExceptionHandler.ErrorResult;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@Component
public class ResponseBodyMatchers {

	@Autowired ObjectMapper objectMapper = createObjectMapper();

	private ObjectMapper createObjectMapper() { //TODO FIND A WAY TO USE THE OBJECT MAPPER IN GeneralConfigurations
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(MapperFeature.DEFAULT_VIEW_INCLUSION, false);
		objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		objectMapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);

		objectMapper.registerModule(new JavaTimeModule());
		
		SimpleModule bigDecimalModule = new SimpleModule();
		bigDecimalModule.addSerializer(BigDecimal.class, new ToStringSerializer());
		objectMapper.registerModule(bigDecimalModule);

		return objectMapper;
	}

	public ResultMatcher isEqualTo(Object expectedObject) {
		return mvcResult -> {
			String responseBody = mvcResult.getResponse().getContentAsString();
			JSONAssert.assertEquals(asJSON(expectedObject), responseBody, false);
		};
	}
	
	public <T> ResultMatcher containsObjectAsJson(Object expectedObject, 
			Class<T> targetClass) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			T responseObject = objectMapper.readValue(json, targetClass);
			assertThat(responseObject).isEqualToComparingFieldByField(expectedObject);
		};
	}
	
	public ResultMatcher notFound(Class<?> type, Object id) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			ErrorResult expected = objectMapper.readValue(json, ErrorResult.class);
			String exMsg = ExceptionSupplier.notFound(type, id).get().getMessage();
			
			assertThat(mvcResult.getResponse().getStatus()).isEqualTo(404);
			assertThat(expected.getError()).isEqualTo("Object Not Found Error");
			assertThat(expected.getMessage()).isEqualTo(exMsg);
			assertThat(expected.getStatus()).isEqualTo(404);
		};
	}

	protected String asJSON(Object expectedObject) throws JsonProcessingException, JSONException {
		JSONObject json = new JSONObject(objectMapper.writeValueAsString(expectedObject));
		
		JSONArray linksArray = json.getJSONArray("links");
		JSONObject links = new JSONObject();
		
		for (int i = 0; i < linksArray.length(); i++) {
			JSONObject item = new JSONObject(linksArray.get(i).toString());
			String rel = item.getString("rel");
			String href = item.getString("href");
			JSONObject value = new JSONObject();
			value.put("href", "http://localhost"+href);
			links.put(rel, value);
		}
		
		json.remove("links");
		json.put("_links", links);
		
		return json.toString().replace("\\/", "/");
	}

	//	public ResultMatcher containsError(String expectedFieldName, 
	//			String expectedMessage) {
	//		return mvcResult -> {
	//			String json = mvcResult.getResponse().getContentAsString();
	//			ErrorResult errorResult = objectMapper.readValue(json, ErrorResult.class);
	//			List<FieldValidationError> fieldErrors = errorResult.getFieldErrors().stream()
	//					.filter(fieldError -> fieldError.getField().equals(expectedFieldName))
	//					.filter(fieldError -> fieldError.getMessage().equals(expectedMessage))
	//					.collect(Collectors.toList());
	//
	//			assertThat(fieldErrors)
	//			.hasSize(1)
	//			.withFailMessage("expecting exactly 1 error message"
	//					+ "with field name '%s' and message '%s'",
	//					expectedFieldName,
	//					expectedMessage);
	//		};
	//	}

	public static ResponseBodyMatchers responseBody(){
		return new ResponseBodyMatchers();
	}
}
