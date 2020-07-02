package com.breakoutms.lfs.server.common;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;
import org.springframework.test.web.servlet.ResultMatcher;

import com.breakoutms.lfs.server.exceptions.CentralExceptionHandler.ErrorResult;
import com.breakoutms.lfs.server.exceptions.CentralExceptionHandler.InvalidFieldError;
import com.breakoutms.lfs.server.exceptions.ExceptionSupplier;
import com.breakoutms.lfs.server.util.WordUtils;
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

	public ResultMatcher isEqualTo(Object expectedObject) {
		return mvcResult -> {
			String responseJson = mvcResult.getResponse().getContentAsString();
			String expectedJSon = objectToJSON(expectedObject);
			responseJson = removeLinks(responseJson);
			expectedJSon = removeLinks(expectedJSon);
			
			JSONAssert.assertEquals(expectedJSon, responseJson, JSONCompareMode.NON_EXTENSIBLE);
		};
	}

	public <T> ResultMatcher isEqualToComparingFieldByField(Object expectedObject, 
			Class<T> targetClass) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			T responseObject = objectMapper.readValue(json, targetClass);
			assertThat(responseObject).isEqualToComparingFieldByField(expectedObject);
		};
	}

	public ResultMatcher isPagedModel() {
		return mvcResult -> {
			String format = "PagedModel should have 'page.%s'";
			String json = mvcResult.getResponse().getContentAsString();
			JSONObject obj = new JSONObject(json);
			JSONObject page = obj.getJSONObject("page");

			assertThat(page).isNotNull();
			assertTrue("PagedModel should have '_embedded' key", obj.has("_embedded"));
			assertTrue(String.format(format, "size"), page.has("size"));
			assertTrue(String.format(format, "totalElements"), page.has("totalElements"));
			assertTrue(String.format(format, "totalPages"), page.has("totalPages"));
			assertTrue(String.format(format, "number"), page.has("number"));
		};
	}

	public PageModelSizeMatchers pageSize() {
		return new PageModelSizeMatchers();
	}

	public PageModelMatchers pagedModel(String collectionRelation) {
		return new PageModelMatchers(collectionRelation);
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
	
	public ResultMatcher hasLink(String rel, String path) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			Map<String, String> links = getLinks(json);
			assertThat(links).containsEntry(rel, path);
		};
	}

	public ResultMatcher containsErrorFor(String fieldName) {
		return mvcResult -> {
			String json = mvcResult.getResponse().getContentAsString();
			ErrorResult response = null;
			try {
				response = objectMapper.readValue(json, ErrorResult.class);
			} catch (Exception e) {
				System.err.println("Unable to parse JSON Response: '"+json+"'");
			}
			assertNotNull(response);
			assertThat(response).withFailMessage("Expecting response (ErrorResult) "
					+ "not to be null for fieldName '%s'", fieldName).isNotNull();
			assertThat(response.getFieldErrors()).withFailMessage("Expecting response.getFieldErrors() "
					+ "not to be null for fieldName '%s'", fieldName).isNotNull();
			List<String> fieldWithErrors = response.getFieldErrors()
					.stream()
					.map(InvalidFieldError::getFieldName)
					.filter(it -> it.equals(fieldName))
					.collect(Collectors.toList());
			
			String errors = response.getFieldErrors().stream()
					.map(Object::toString)
		            .collect(Collectors.joining(", "));
			
			assertThat(fieldWithErrors).withFailMessage("Expecting 1 error message "
						+ "for field '%s', but was %d errors: %s", 
						fieldName, fieldWithErrors.size(), errors).hasSize(1);
			assertThat(fieldWithErrors).containsOnly(fieldName);
			assertThat(response.getMessage()).contains("Invalid input for", 
					WordUtils.humenize(fieldName));
		};
	}
	
	private String removeLinks(String json) throws JSONException {
		JSONObject jObj = new JSONObject(json);
		jObj.remove("_links");
		return jObj.toString();
	}
	
	private Map<String, String> getLinks(String json) throws JSONException {
		Map<String, String> map = new HashMap<>();
		JSONObject jObj = new JSONObject(json);
		JSONObject links = jObj.getJSONObject("_links");
		JSONArray names = links.names();
		for (int i = 0; i < names.length(); i++) {
			String rel = names.getString(i);
			String href = links.getString(rel).replace("\\/", "/");
			href = href.replace("\"href\":\"http://localhost", "");
			href = href.replaceAll("\"", "").replace("{", "").replace("}", "");
			map.put(rel, href);
		}
		return map;
	}

	protected String objectToJSON(Object expectedObject) throws JsonProcessingException, JSONException {
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
		if(links.length() > 0) {
			json.put("_links", links);
		}

		return json.toString().replace("\\/", "/");
	}

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

	public static ResponseBodyMatchers responseBody(){
		return new ResponseBodyMatchers();
	}

	public class PageModelMatchers {
		private String collectionRelation;
		public PageModelMatchers(String collectionRelation) {
			this.collectionRelation = collectionRelation;
		}

		public ResultMatcher contains(RepresentationModel<?> viewModel) {
			return mvcResult -> {
				String json = mvcResult.getResponse().getContentAsString();
				JSONObject _embedded = new JSONObject(json).getJSONObject("_embedded");
				assertThat(_embedded.has(collectionRelation))
					.withFailMessage("Response JSON body does not have node with name '%s'", collectionRelation)
					.isTrue();
				JSONArray array = _embedded.getJSONArray(collectionRelation);
				//TODO: THIS ONLY ASSUMES VIEW MODEL IS IN THE FIRST ELEMENT OF THE ARRAY
				String item = array.getJSONObject(0).toString();

				JSONAssert.assertEquals(objectToJSON(viewModel), item, false);
			};
		}
	}

	public class PageModelSizeMatchers {
		public ResultMatcher isEqualTo(int size) {
			return mvcResult -> {
				String json = mvcResult.getResponse().getContentAsString();
				JSONObject page = new JSONObject(json).getJSONObject("page");

				assertThat(page).isNotNull();
				assertThat(page.get("totalElements")).isEqualTo(size);
			};
		}
	}
}
