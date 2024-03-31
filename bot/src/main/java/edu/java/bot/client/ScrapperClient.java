package edu.java.bot.client;

import edu.java.bot.client.retryModel.CustomRetryPolicy;
import edu.java.bot.client.retryModel.RetryPolicyParameters;
import edu.java.bot.configuration.RetryPolicyConfig;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.ListLinksResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import io.github.resilience4j.retry.Retry;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import java.util.Set;

public class ScrapperClient {
    private final WebClient webClient;
    private Retry retry;
    @Value(value = "${api.scrapper.retryPolicy}")
    private CustomRetryPolicy policy;
    @Value(value = "${api.scrapper.retryNumber}")
    private int number;
    @Value("#{'${api.scrapper.statuses}'.split(',')}")
    private Set<HttpStatus> statuses;
    @Value("api-scrapper-base-url")
    private String scrapperBaseUrl;
    private final String links = "/links";
    private final String tgChat = "/tg-chat/{id}";
    private final String tgChatId = "Tg-Chat-Id";

    public ScrapperClient(String baseUrl) {
        String url = baseUrl;
        if (baseUrl.isEmpty()) {
            url = scrapperBaseUrl;
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

    public String registerChat(Long id) {
        return webClient
            .post()
            .uri(uriBuilder -> uriBuilder.path(tgChat).build(id))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }
    public String deleteChat(Long id) {
        return webClient
            .delete()
            .uri(uriBuilder -> uriBuilder.path(tgChat).build(id))
            .retrieve()
            .bodyToMono(String.class)
            .block();
    }

    public ListLinksResponse getLinks(Long id) {
        return webClient
            .get()
            .uri(links)
            .header(tgChatId, String.valueOf(id))
            .retrieve()
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
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public LinkResponse removeLink(Long id, RemoveLinkRequest request) {
        return webClient.method(HttpMethod.DELETE)
            .uri(links)
            .header(tgChatId, String.valueOf(id))
            .body(BodyInserters.fromValue(request))
            .retrieve()
            .bodyToMono(LinkResponse.class)
            .block();
    }

    public String retryRegisterChat(Long chatId) {
        return Retry.decorateSupplier(retry, () -> registerChat(chatId)).get();
    }

    public String retryDeleteChat(Long chatId) {
        return Retry.decorateSupplier(retry, () -> deleteChat(chatId)).get();
    }

    public ListLinksResponse retryGetLinks(Long chatId) {
        return Retry.decorateSupplier(retry, () -> getLinks(chatId)).get();
    }

    public LinkResponse retryAddLink(Long chatId, AddLinkRequest request) {
        return Retry.decorateSupplier(retry, () -> addLink(chatId, request)).get();
    }

    public LinkResponse retryRemoveLink(Long chatId, RemoveLinkRequest request) {
        return Retry.decorateSupplier(retry, () -> removeLink(chatId, request)).get();
    }
}
