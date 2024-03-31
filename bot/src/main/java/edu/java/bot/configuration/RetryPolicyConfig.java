package edu.java.bot.configuration;

import edu.java.bot.client.retryModel.RetryPolicyParameters;
import io.github.resilience4j.core.IntervalFunction;
import io.github.resilience4j.retry.Retry;
import io.github.resilience4j.retry.RetryConfig;
import java.time.Duration;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.client.WebClientResponseException;

public class RetryPolicyConfig {
    @Value(value = "${api.scrapper.interval}")
    private static int interval;

    private RetryPolicyConfig() {
    }

    public static Retry config(RetryPolicyParameters settings) {
        RetryConfig config = switch (settings.getPolicy()) {
            case CONSTANT -> constant(settings);
            case LINEAR -> linear(settings);
            case EXPONENTIAL -> exponential(settings);
        };

        return Retry.of("bot-retry-" + OffsetDateTime.now(), config);
    }

    private static RetryConfig constant(RetryPolicyParameters settings) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(settings.getNumber())
            .waitDuration(Duration.ofSeconds(interval))
            .retryOnResult(response -> settings.getStatuses().contains(response.getStatusCode()))
            .build();
    }

    private static RetryConfig linear(RetryPolicyParameters settings) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(settings.getNumber())
            .intervalFunction(IntervalFunction.of(
                Duration.ofSeconds(interval),
                attempt -> interval + attempt * interval
            ))
            .retryOnResult(response -> settings.getStatuses().contains(response.getStatusCode()))
            .build();
    }

    private static RetryConfig exponential(RetryPolicyParameters settings) {
        return RetryConfig.<WebClientResponseException>custom()
            .maxAttempts(settings.getNumber())
            .intervalFunction(IntervalFunction.ofExponentialBackoff(
                IntervalFunction.DEFAULT_INITIAL_INTERVAL,
                IntervalFunction.DEFAULT_MULTIPLIER
            ))
            .retryOnResult(response -> settings.getStatuses().contains(response.getStatusCode()))
            .build();
    }
}
