package dev.codingstoic.newsaggregator.dto;

import lombok.Getter;

@Getter
public enum SortByEnum {
    RELEVANCY("relevancy"),
    POPULARITY("popularity"),
    PUBLISHED_AT("publishedAt");
    public static final String DEFAULT_SORT = "publishedAt";

    private final String value;

    SortByEnum(String value) {
        this.value = value;
    }

    public static SortByEnum fromValue(String value) {
        for (SortByEnum sortBy : values()) {
            if (sortBy.value.equalsIgnoreCase(value)) {
                return sortBy;
            }
        }
        throw new IllegalArgumentException("Unknown value: " + value);
    }
}
