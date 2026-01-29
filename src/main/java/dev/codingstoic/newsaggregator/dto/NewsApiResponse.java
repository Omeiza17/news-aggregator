package dev.codingstoic.newsaggregator.dto;

import java.util.List;

public record NewsApiResponse(String status, int totalResults, List<Article> articles) {
}
