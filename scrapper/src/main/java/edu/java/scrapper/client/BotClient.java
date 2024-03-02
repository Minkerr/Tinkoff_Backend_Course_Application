package edu.java.scrapper.client;

import edu.java.scrapper.dto.ApiErrorException;
import edu.java.scrapper.dto.ApiErrorResponse;
import edu.java.scrapper.dto.LinkUpdateRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class BotClient {
    private final String botBaseUrl = "localhost";
    private final WebClient webClient;

    public BotClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = botBaseUrl;
        }
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public LinkUpdateRequest sendUpdate(LinkUpdateRequest request) {
        return webClient
            .post()
            .uri("/updates")
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatus.BAD_REQUEST::equals,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(LinkUpdateRequest.class)
            .block();
    }
}
