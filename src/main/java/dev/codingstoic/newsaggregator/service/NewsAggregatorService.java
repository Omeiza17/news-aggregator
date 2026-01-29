package dev.codingstoic.newsaggregator.service;

import dev.codingstoic.newsaggregator.config.NewsApiClient;
import dev.codingstoic.newsaggregator.dto.Article;
import dev.codingstoic.newsaggregator.dto.NewsApiResponse;
import dev.codingstoic.newsaggregator.dto.SortByEnum;
import io.micrometer.common.util.StringUtils;
import io.micrometer.context.ContextSnapshot;
import io.micrometer.context.ContextSnapshotFactory;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.StructuredTaskScope;
import java.util.stream.Collectors;

import static dev.codingstoic.newsaggregator.dto.SortByEnum.RELEVANCY;

@Slf4j
@Service
@RequiredArgsConstructor
@Observed(name = "news.aggregation")
public class NewsAggregatorService {
    private static final int DEFAULT_PAGE_SIZE = 10;
    private static final String DEFAULT_COUNTRY = "uk";
    private final NewsApiClient newsApiClient;

    private static final ContextSnapshotFactory snapshotFactory = ContextSnapshotFactory.builder().build();


    public List<Article> collectNewsFromAllSources() {
        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {
            StructuredTaskScope.Subtask<NewsApiResponse> taskTech = scope.fork(
                    () -> newsApiClient.searchNews("technology", null, null, null, null, null)
            );

            StructuredTaskScope.Subtask<NewsApiResponse> taskBusiness = scope.fork(
                    () -> newsApiClient.getTopHeadlines(DEFAULT_COUNTRY, "business", "Interest rate")
            );

            StructuredTaskScope.Subtask<NewsApiResponse> taskAI = scope.fork(
                    () -> newsApiClient.searchNews("Artificial Intelligence", null, null, null, null, RELEVANCY.getValue())
            );

            scope.join();

            List<Article> aggregatedNews = new ArrayList<>();
            aggregatedNews.addAll(extractArticles(taskTech));
            aggregatedNews.addAll(extractArticles(taskBusiness));
            aggregatedNews.addAll(extractArticles(taskAI));

            return aggregatedNews;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Aggregation was interrupted", e);
        }
    }

    private List<Article> extractArticles(StructuredTaskScope.Subtask<NewsApiResponse> task) {
        if (task.state() == StructuredTaskScope.Subtask.State.SUCCESS) {
            return task.get().articles();
        } else {
            log.error("Failed with status {}", task.get().status(), task.exception());
            return Collections.emptyList();
        }
    }


    public List<Article> getCustomNewFeed(List<String> topics, String language, String sortBy, String from, String to) {
        ContextSnapshot snapshot = snapshotFactory.captureAll();

        try (var scope = StructuredTaskScope.open(StructuredTaskScope.Joiner.awaitAll())) {

            List<StructuredTaskScope.Subtask<NewsApiResponse>> searchTask = new ArrayList<>();

            for (var topic : topics) {
                searchTask.add(
                        scope.fork(snapshot.wrap(() -> {
                            log.info("I am running in a virtual thread!");
                            return newsApiClient.searchNews(topic, StringUtils.isEmpty(language) ? null : language, DEFAULT_PAGE_SIZE, from, to, StringUtils.isEmpty(sortBy) ? null : SortByEnum.fromValue(sortBy).getValue());
                        }))
                );
            }

            scope.join();

            return searchTask.stream().flatMap(task -> extractArticles(task).stream()).collect(Collectors.toList());


        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Custom feed aggregation interrupted", e);
        }
    }
}
