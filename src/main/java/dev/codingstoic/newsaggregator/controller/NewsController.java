package dev.codingstoic.newsaggregator.controller;

import dev.codingstoic.newsaggregator.dto.Article;
import dev.codingstoic.newsaggregator.service.NewsAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsAggregatorService newsAggregatorService;

    @GetMapping
    public List<Article> getAggregatedNews() {

        return newsAggregatorService.collectNewsFromAllSources();
    }

}

