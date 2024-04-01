package edu.java.scrapper.client;

import edu.java.scrapper.client.retryModel.CustomRetryPolicy;
import edu.java.scrapper.client.retryModel.RetryPolicyParameters;
import edu.java.scrapper.configuration.RetryPolicyConfig;
import edu.java.scrapper.response.QuestionResponse;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import java.util.Set;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;

public class StackOverflowClient {
    @Value("${api.stackoverflow.base-url}")
    private String stackOverflowBaseUrl;
    private final WebClient webClient;
    private Retry retry;
    @Value(value = "${api.stackoverflow.retryPolicy}")
    private CustomRetryPolicy policy;
    @Value(value = "${api.stackoverflow.retryNumber}")
    private int number;
    @Value("#{'${api.stackoverflow.statuses}'.split(',')}")
    private Set<HttpStatus> statuses;

    public StackOverflowClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = stackOverflowBaseUrl;
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

    public QuestionResponse getQuestion(long id) {
        return webClient
            .get()
            .uri("/2.3/questions/{id}?order=desc&sort=activity&site=stackoverflow", id)
            .retrieve()
            .bodyToMono(QuestionResponse.class)
            .block();
    }

    public QuestionResponse retryGetQuestion(long id) {
        return Retry.decorateSupplier(retry, () -> getQuestion(id)).get();
    }
}
