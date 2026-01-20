package dev.codingstoic.newsaggregator.config;

import dev.codingstoic.newsaggregator.dto.NewsResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/v2", accept = "application/json")
public interface NewsApiClient {

    @GetExchange("/everything")
    NewsResponse searchNews(
            @RequestParam("q") String query,
            @RequestParam("language") String language,
            @RequestParam(value = "pageSize", defaultValue = "20") int pageSize,
            @RequestParam("apiKey") String apiKey);

    @GetExchange("/top-headlines")
    NewsResponse getTopHeadlines(
            @RequestParam("country") String country,
            @RequestParam("category") String category,
            @RequestParam("language") String language,
            @RequestParam("apiKey") String apiKey
    );
}
