package com.stalin.demo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.extension.ExtendWith; // MODERNIZATION: Changed from JUnit 4
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension; // MODERNIZATION: Changed from JUnit 4
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// MODERNIZATION: Imports for 'javax.servlet' are gone,
// as Spring Boot 3 handles this automatically.

import java.io.IOException;

//
// MODERNIZATION:
// Updated test runner from JUnit 4 (@RunWith(SpringJUnit4ClassRunner.class))
// to the JUnit 5 equivalent.
//
@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = DemoWorkshopApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    protected void setUp() {
        mvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();
    }
    protected String mapToJson(Object obj) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(obj);
    }
    protected <T> T mapFromJson(String json, Class<T> clazz)
            throws JsonParseException, JsonMappingException, IOException {

        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(json, clazz);
    }
}
