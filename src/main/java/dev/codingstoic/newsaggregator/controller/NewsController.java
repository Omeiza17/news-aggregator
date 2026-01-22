package dev.codingstoic.newsaggregator.controller;

import dev.codingstoic.newsaggregator.dto.Article;
import dev.codingstoic.newsaggregator.service.NewsAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/news")
public class NewsController {

    private final NewsAggregatorService newsAggregatorService;

    @GetMapping
    public List<Article> getDailyBriefing() {

        return newsAggregatorService.collectNewsFromAllSources();
    }

    @GetMapping("/custom")
    public List<Article> getCustomFeed(
            @RequestParam(defaultValue = "technology") List<String> queryTopics,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return newsAggregatorService.getCustomNewFeed(queryTopics, language, sortBy, from, to);
    }

}

