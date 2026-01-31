package dev.codingstoic.newsaggregator.dto;

import java.io.Serializable;

public record Article(
        Source source,
        String author,
        String title,
        String description,
        String url,
        String urlToImage,
        String publishedAt,
        String content) implements Serializable {
}
