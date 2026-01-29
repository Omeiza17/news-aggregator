package dev.codingstoic.newsaggregator.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import dev.codingstoic.newsaggregator.service.NewsAggregatorService;
import dev.codingstoic.newsaggregator.dto.Article;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class NewsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private NewsAggregatorService newsAggregatorService;

    @Test
    void shouldGetV1ByDefault() throws Exception {
        when(newsAggregatorService.collectNewsFromAllSources()).thenReturn(List.of());

        mockMvc.perform(get("/api/news"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetV1Explicitly() throws Exception {
        when(newsAggregatorService.collectNewsFromAllSources()).thenReturn(List.of());

        mockMvc.perform(get("/api/news")
                .header("X-API-VERSION", "1.0"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetV2Explicitly() throws Exception {
        mockMvc.perform(get("/api/news")
                .header("X-API-VERSION", "2.0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("V2 Content"));
    }
}
