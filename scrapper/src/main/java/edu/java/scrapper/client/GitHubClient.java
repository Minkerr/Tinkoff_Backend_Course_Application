package edu.java.scrapper.client;

import edu.java.scrapper.client.retryModel.CustomRetryPolicy;
import edu.java.scrapper.client.retryModel.RetryPolicyParameters;
import edu.java.scrapper.configuration.RetryPolicyConfig;
import edu.java.scrapper.dto.LinkUpdateRequest;
import edu.java.scrapper.response.RepositoryResponse;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Set;

public class GitHubClient {
    @Value("${api.github.base-url}")
    private String gitHubBaseUrl;
    private final WebClient webClient;
    private Retry retry;
    @Value(value = "${api.github.retryPolicy}")
    private CustomRetryPolicy policy;
    @Value(value = "${api.github.retryNumber}")
    private int number;
    @Value("#{'${api.github.statuses}'.split(',')}")
    private Set<HttpStatus> statuses;

    public GitHubClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = gitHubBaseUrl;
        }
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    @PostConstruct
    private void configRetry() {
        RetryPolicyParameters retryPolicySettings = new RetryPolicyParameters();
        retryPolicySettings.setPolicy(policy);
        retryPolicySettings.setNumber(number);
        retryPolicySettings.setStatuses(statuses);
        retry = RetryPolicyConfig.config(retryPolicySettings);
    }

    public RepositoryResponse getRepository(String user, String repository) {
        return webClient
            .get()
            .uri("/repos/{user}/{repository}", user, repository)
            .retrieve()
            .bodyToMono(RepositoryResponse.class)
            .block();
    }

    public RepositoryResponse retryGetRepository(String user, String repository) {
        return Retry.decorateSupplier(retry, () -> getRepository(user, repository)).get();
    }
}
