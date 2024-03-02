package edu.java.bot.client;

import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.ApiErrorException;
import edu.java.bot.dto.ApiErrorResponse;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

public class ScrapperClient {
    private final WebClient webClient;
    private final String scrapperBaseUrl = "localhost";
    private final String links = "/links";
    private final String tgChat = "tg-chat/{id}";
    private final String tgChatId = "Tg-Chat-Id";

    public ScrapperClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = scrapperBaseUrl;
        }
        this.webClient = WebClient.builder().baseUrl(url).build();
    }

    public void registerChat(Long id) {
        webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(tgChat).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

    public void deleteChat(Long id) {
        webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(tgChat).build(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(String.class)
            .blockOptional();
    }

    public ListLinksResponse getLinks(Long id) {
        return webClient
            .get()
            .uri(links)
            .header(tgChatId, String.valueOf(id))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(ListLinksResponse.class)
            .block();
    }

    public LinkResponse addLink(Long id, AddLinkRequest request) {
        return webClient
            .post()
            .uri(links)
            .header(tgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(links)
            .header(tgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .onStatus(
                HttpStatusCode::is4xxClientError,
                response -> response
                    .bodyToMono(ApiErrorResponse.class)
                    .flatMap(errorResponse -> Mono.error(new ApiErrorException(errorResponse)))
            )
            .bodyToMono(LinkResponse.class)
            .block();
    }
}
