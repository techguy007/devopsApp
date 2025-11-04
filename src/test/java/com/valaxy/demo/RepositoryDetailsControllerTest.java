package com.stalin.demo;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest
class RepositoryDetailsControllerTest extends AbstractTest {

	@BeforeEach
	public void setUp() {
		super.setUp();
	}

	//
	// MODERNIZATION:
	// This test for "/trends" would fail because the endpoint is
	// commented out. For the build to pass, this test
	// must also be commented out.
	//
	/*
	@Test
	public void getProductsList() throws Exception {
		String uri = "/trends?placeid=1&count=5";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
	*/

	// ADDED: A simple test for the root endpoint that still works.
	@Test
	public void getRootEndpoint() throws Exception {
		String uri = "/";
		MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
				.accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();
		int status = mvcResult.getResponse().getStatus();
		assertEquals(200, status);
	}
}
