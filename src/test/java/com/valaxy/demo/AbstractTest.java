package com.stalin.demo;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
// import org.junit.runner.RunWith; // JUnit 4
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.test.context.junit4.SpringJUnit4ClassRunner; // JUnit 4
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

// Spring Boot 3 / JUnit 5 imports
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.junit.jupiter.SpringExtension;

// 
// CHANGE 7: Migrated test classes from JUnit 4 to JUnit 5
//
// @RunWith(SpringJUnit4ClassRunner.class) // This is JUnit 4
@ExtendWith(SpringExtension.class) // This is the JUnit 5 equivalent
@SpringBootTest(classes = DemoWorkshopApplication.class)
@WebAppConfiguration
public abstract class AbstractTest {

    protected MockMvc mvc;
    @Autowired
    WebApplicationContext webApplicationContext;

    // This method is now called by @BeforeEach in the child class
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
