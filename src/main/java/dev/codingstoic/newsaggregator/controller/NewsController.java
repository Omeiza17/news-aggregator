package dev.codingstoic.newsaggregator.controller;

import dev.codingstoic.newsaggregator.dto.NewsResponse;
import dev.codingstoic.newsaggregator.dto.NewsResponseV1;
import dev.codingstoic.newsaggregator.dto.NewsResponseV2;
import dev.codingstoic.newsaggregator.service.NewsAggregatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/api/news")
public class NewsController {

    private final NewsAggregatorService newsAggregatorService;

    @GetMapping(version = "1.0")
    public List<NewsResponse> getDailyBriefing() {

        return newsAggregatorService
                .collectNewsFromAllSources()
                .stream()
                .map(article -> (NewsResponse) new NewsResponseV1(article.title(), article.url(), article.author(), article.source().name(), article.publishedAt()))
                .toList();
    }

    @GetMapping(version = "2.0")
    public List<NewsResponse> getDailyBriefingV2() {
        return newsAggregatorService
                .collectNewsFromAllSources()
                .stream()
                .map(article -> (NewsResponse) new NewsResponseV2(article.title(), article.url(), article.author(), article.publishedAt(), "POSITIVE", article.source().name()))
                .toList();
    }

    @GetMapping(path = "/custom", version = "1.0")
    public List<NewsResponse> getCustomFeed(
            @RequestParam(defaultValue = "technology") List<String> queryTopics,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return newsAggregatorService.getCustomNewFeed(queryTopics, language, sortBy, from, to)
                .parallelStream()
                .map(article -> (NewsResponse) new NewsResponseV1(article.title(), article.url(), article.author(), article.source().name(), article.publishedAt()))
                .toList();
    }

    @GetMapping(path = "/custom", version = "2.0")
    public List<NewsResponse> getCustomFeedVV2(
            @RequestParam(defaultValue = "technology") List<String> queryTopics,
            @RequestParam(required = false) String language,
            @RequestParam(required = false) String sortBy,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to
    ) {
        return newsAggregatorService.getCustomNewFeed(queryTopics, language, sortBy, from, to)
                .parallelStream()
                .map(article -> (NewsResponse) new NewsResponseV2(article.title(), article.url(), article.author(), article.publishedAt(), "POSITIVE", article.source().name()))
                .toList();
    }

}
