package com.capgemini.testcasesdemo;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.capgemini.testcasesdemo.TestCasesDemoApplication;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = TestCasesDemoApplication.class)
public class TestCasesDemoApplicationTests {

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	private ObjectMapper mapper = new ObjectMapper();

	private Resource casesFile;

	private Map<String, Json> cases;

	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();
		casesFile = new ClassPathResource("cases.json");

		cases = mapper.readValue(casesFile.getInputStream(), new TypeReference<Map<String, Json>>() {
		});
	}

	@Test
	public void test1() throws Exception {
		Json json = cases.get("demo");
		test(json);
	}
	
	@Test
	public void test2() throws Exception {
		Json json = cases.get("demo2");
		test(json);
	}
	
	@Test
	public void test4() throws Exception {
		Json json = cases.get("mydemo");
		test(json);
	}

	private void test(Json json) throws Exception {
		ResultActions actions = mockMvc
				.perform(getMethod(json).headers(json.getRequest().getHeaders()).contentType(MediaType.APPLICATION_JSON)
						.content(getRequestBody(json)).accept(MediaType.APPLICATION_JSON));

		actions.andExpect(status().is(json.getResponse().getStatus().value()));

		MockHttpServletResponse response = actions.andReturn().getResponse();

		for (String key : json.getResponse().getHeaders().keySet()) {
			assertEquals(json.getResponse().getHeaders().get(key), response.getHeader(key));
		}
		assertEquals(getResponseBody(json), response.getContentAsString());
	}

	private MockHttpServletRequestBuilder getMethod(Json json) {
		return MockMvcRequestBuilders.request(HttpMethod.resolve(json.getRequest().getMethod()),
				json.getRequest().getUrl());
	}

	private String getRequestBody(Json json) throws JsonProcessingException {
		return mapper.writeValueAsString(json.getRequest().getBody());
	}

	private String getResponseBody(Json json) throws JsonProcessingException {
		if (json.getResponseMessage() != null) {
			return json.getResponseMessage();
		}
		return mapper.writeValueAsString(json.getResponse().getBody());
	}

}
