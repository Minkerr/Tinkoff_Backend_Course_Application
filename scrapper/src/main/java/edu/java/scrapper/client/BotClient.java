package edu.java.scrapper.client;

import edu.java.scrapper.client.retryModel.CustomRetryPolicy;
import edu.java.scrapper.client.retryModel.RetryPolicyParameters;
import edu.java.scrapper.configuration.RetryPolicyConfig;
import edu.java.scrapper.dto.LinkUpdateRequest;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Set;

public class BotClient {
    @Value("api-bot-base-url")
    private String botBaseUrl;
    private final WebClient webClient;
    private Retry retry;
    @Value(value = "${api.bot.retryPolicy}")
    private CustomRetryPolicy policy;
    @Value(value = "${api.bot.retryNumber}")
    private int number;
    @Value("#{'${api.bot.statuses}'.split(',')}")
    private Set<HttpStatus> statuses;

    public BotClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = botBaseUrl;
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

    public String sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public String retrySendUpdate(LinkUpdateRequest request) {
        return Retry.decorateSupplier(retry, () -> sendUpdate(request)).get();
    }
}
