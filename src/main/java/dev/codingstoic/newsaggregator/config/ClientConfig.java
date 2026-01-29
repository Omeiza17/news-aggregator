package dev.codingstoic.newsaggregator.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.net.http.HttpClient;
import java.time.Duration;
import java.util.concurrent.Executors;

@Configuration
public class ClientConfig {
    @Bean
    public NewsApiClient newsApiClient(@Value("${news.api.key}") String apiToken) {

        HttpClient sharedHttpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .connectTimeout(Duration.ofSeconds(10))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .executor(Executors.newVirtualThreadPerTaskExecutor())
                .build();

        JdkClientHttpRequestFactory requestFactory = new JdkClientHttpRequestFactory(sharedHttpClient);
        requestFactory.setReadTimeout(Duration.ofSeconds(10));

        RestClient restClient = RestClient.builder()
                .baseUrl("https://newsapi.org")
                .defaultHeader("X-Api-Key", apiToken)
                .requestFactory(requestFactory)
                .build();


        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(NewsApiClient.class);
    }
}
