package dev.codingstoic.newsaggregator.dto;

public record Article(
        Source source,
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        String publishedAt,
        String content) {
}
