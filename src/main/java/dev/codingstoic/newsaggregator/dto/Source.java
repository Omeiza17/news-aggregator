package dev.codingstoic.newsaggregator.dto;

import java.io.Serializable;

public record Source(String id, String name) implements Serializable {
}
