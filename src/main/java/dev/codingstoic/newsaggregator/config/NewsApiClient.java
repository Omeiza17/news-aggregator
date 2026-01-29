package dev.codingstoic.newsaggregator.config;

import dev.codingstoic.newsaggregator.dto.NewsApiResponse;
import dev.codingstoic.newsaggregator.dto.SortByEnum;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

@HttpExchange(url = "/v2", accept = "application/json")
public interface NewsApiClient {

    @GetExchange("/everything")
    NewsApiResponse searchNews(
            @RequestParam(value = "q") String query,
            @RequestParam(value = "language", required = false, defaultValue = "en") String language,
            @RequestParam(value = "pageSize", required = false, defaultValue = "20") Integer pageSize,
            @RequestParam(value = "from", required = false) String from,
            @RequestParam(defaultValue = "to", required = false) String to,
            @RequestParam(value = "sortBy", required = false, defaultValue = SortByEnum.DEFAULT_SORT) String sortBy
    );

    @GetExchange("/top-headlines")
    NewsApiResponse getTopHeadlines(
            @RequestParam("country") String country,
            @RequestParam("category") String category,
            @RequestParam("q") String q
    );
}
