package dev.codingstoic.newsaggregator.dto;

public sealed interface NewsResponse permits NewsResponseV1, NewsResponseV2 {
}
