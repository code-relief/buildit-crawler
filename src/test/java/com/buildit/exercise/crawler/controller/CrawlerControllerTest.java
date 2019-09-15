package com.buildit.exercise.crawler.controller;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CrawlerControllerTest {

    private static final String PARSER_PATH = "/crawl";

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    @Before
    public void setUp() {
        mvc = MockMvcBuilders
                .webAppContextSetup(context)
                .build();
    }

    @Test
    public void testBasicRequestResponse() throws Exception {
        String url = "http://wiprodigital.com";
        String expectedResult = String.format("{\"externalLinks\": [],\"internalLinks\": [],\"staticLinks\": [],\"url\": \"%s\"}", url);
        mvc.perform(MockMvcRequestBuilders
                .post(PARSER_PATH)
                .content(url)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

    @Test
    public void testIncorrectUrlErrorHandling() throws Exception {
        String url = "malformed_url";
        String expectedResult = "{\"details\": \"\",\"message\": 'crawlerEndpoint.url: must match \"^(https?)://[a-zA-Z0-9][a-zA-Z0-9\\\\.&\\\\?#/%=]*\"'}";
        mvc.perform(MockMvcRequestBuilders
                .post(PARSER_PATH)
                .content(url)
                .contentType(MediaType.TEXT_PLAIN)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.content().json(expectedResult));
    }

}
