package edu.java.scrapper.client;

import edu.java.scrapper.dto.LinkUpdateRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

public class BotClient {
    @Value("api-bot-base-url")
    private String botBaseUrl;
    private final WebClient webClient;

    public BotClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = botBaseUrl;
        }
        this.webClient = WebClient.builder().baseUrl(url).build();
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
}
