package dev.codingstoic.newsaggregator.dto;

import java.util.List;

public record NewsResponse(String status, int totalResults, List<Article> articles) {
}
