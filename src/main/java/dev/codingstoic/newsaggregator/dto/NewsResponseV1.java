package dev.codingstoic.newsaggregator.dto;

public record NewsResponseV1(
        String title,
        String url,
        String author,
        String source,
        String publishedAt
) implements NewsResponse {
}
