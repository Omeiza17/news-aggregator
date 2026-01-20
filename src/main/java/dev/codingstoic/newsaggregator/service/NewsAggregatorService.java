package dev.codingstoic.newsaggregator.service;

import dev.codingstoic.newsaggregator.config.NewsApiClient;
import dev.codingstoic.newsaggregator.dto.Article;
import dev.codingstoic.newsaggregator.dto.NewsResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;

@Slf4j
@Service
@RequiredArgsConstructor
public class NewsAggregatorService {
    public static final String DEFAULT_LANGUAGE = "en";
    private static final int DEFAULT_PAGE_SIZE = 20;
    private final NewsApiClient newsApiClient;
    @Value("${news.api.key}")
    private String apiKey;


    public List<Article> collectNewsFromAllSources() {
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
            StructuredTaskScope.Subtask<List<Article>> taskTech = scope.fork(() -> fetchCategory("Technology"));
            StructuredTaskScope.Subtask<List<Article>> taskSports = scope.fork(() -> fetchCategory("Sports"));
            StructuredTaskScope.Subtask<List<Article>> taskWorld = scope.fork(() -> fetchCategory("World"));

            scope.join();

            List<Article> aggregatedNews = new ArrayList<>();
            aggregatedNews.addAll(handleResult(taskTech, "Bitcoin"));
            aggregatedNews.addAll(handleResult(taskSports, "Football"));
            aggregatedNews.addAll(handleResult(taskWorld, "Music"));

            return aggregatedNews;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Aggregation was interrupted", e);
        }
    }

    private List<Article> handleResult(StructuredTaskScope.Subtask<List<Article>> task, String sourceName) {
        if (task.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
            return task.get();
        } else {
            log.error("Failed to fetch {}", sourceName, task.exception());
            return List.of();
        }
    }

    private List<Article> fetchCategory(String category) {
        NewsResponse newsResponse = newsApiClient.searchNews(category, DEFAULT_LANGUAGE, DEFAULT_PAGE_SIZE, apiKey);
        log.debug("Response status: {}", newsResponse.status());
        return newsResponse.status().equals("ok") ? newsResponse.articles() : List.of();
    }
}
