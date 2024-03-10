package edu.java.scrapper;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.dto.LinkUpdateRequest;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class BotClientTest {
    private static WireMockServer wireMockServer;
    private static BotClient botClient;
    private static final String baseUrl = "http://localhost:8080";

    @BeforeAll
    public static void setUp() {
        botClient = new BotClient(baseUrl);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void botClient_shouldProcessRequests() {
        //arrange
        LinkUpdateRequest request = new LinkUpdateRequest(
            1L,
            "1",
            "1",
            List.of(1L)
        );
        var expected = "Update has been handled";
        wireMockServer.stubFor(post(urlEqualTo("/updates"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(expected)
            ));
        //act
        var act = botClient.sendUpdate(request);
        //assert
        assertThat(act).isEqualTo(expected);
    }
}
