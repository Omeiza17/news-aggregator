package dev.codingstoic.newsaggregator.dto;

public record NewsResponseV2(
        String headline,
        String link,
        String author,
        String publishedAt,
        String sentiment,
        String source
) implements NewsResponse {
}
