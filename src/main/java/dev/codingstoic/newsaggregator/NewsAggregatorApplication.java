package dev.codingstoic.newsaggregator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;


@EnableCaching
@SpringBootApplication
public class NewsAggregatorApplication {

	static void main(String[] args) {
		SpringApplication.run(NewsAggregatorApplication.class, args);
	}

}
