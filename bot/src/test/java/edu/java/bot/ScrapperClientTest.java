package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.AddLinkRequest;
import edu.java.bot.dto.LinkResponse;
import edu.java.bot.dto.RemoveLinkRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.delete;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.post;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class ScrapperClientTest {
    private static WireMockServer wireMockServer;
    private static ScrapperClient scrapperClient;
    private static final String baseUrl = "http://localhost:8080";

    @BeforeAll
    public static void setUp() {
        scrapperClient = new ScrapperClient(baseUrl);
        wireMockServer = new WireMockServer(8080);
        wireMockServer.start();
    }

    @AfterAll
    public static void tearDown() {
        wireMockServer.stop();
    }

    @Test
    public void scrapperClient_shouldRegisterChat() {
        //arrange
        String excpected = "Chat has been registered";
        wireMockServer.stubFor(post(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(excpected)));

        //act
        var act = scrapperClient.registerChat(1L);
        //assert
        assertThat(act).isEqualTo(excpected);
    }

    @Test
    public void scrapperClient_shouldDeleteChat() {
        //arrange
        String expected = "Chat has been deleted";
        wireMockServer.stubFor(delete(urlEqualTo("/tg-chat/1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(expected)));

        //act
        var act = scrapperClient.deleteChat(1L);
        //assert
        assertThat(act).isEqualTo(expected);
    }

    @Test
    public void scrapperClient_shouldGetLinks() {
        //arrange
        String expected = "link";
        String responseBody = """
            {
                "links":[
                    {
                        "id":1,
                        "url":"link"
                    }
                ],
                "size":1
            }
            """;
        wireMockServer.stubFor(get(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(responseBody)));
        //act
        var act = scrapperClient.getLinks(1L);
        var actUrl = act.links().get(0).url();
        var actSize = act.size();
        //assert
        assertThat(actSize).isEqualTo(1);
        assertThat(actUrl).isEqualTo(expected);
    }

    @Test
    public void scrapperClient_shouldAddLink() {
        //arrange
        String link = """
            {
                "id":1,
                "url":"1"
            }
            """;
        wireMockServer.stubFor(post(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(link)));
        //act
        LinkResponse act = scrapperClient.addLink(1L, new AddLinkRequest("1"));
        //assert
        Assertions.assertThat(act.id()).isEqualTo(1);
        Assertions.assertThat(act.url()).isEqualTo("1");
    }

    @Test
    public void scrapperClient_shouldDeleteLink() {
        //arrange
        String link = """
            {
                "id":1,
                "url":"1"
            }
            """;
        wireMockServer.stubFor(delete(urlEqualTo("/links"))
            .withHeader("Tg-Chat-Id", equalTo("1"))
            .willReturn(aResponse()
                .withStatus(200)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withBody(link)));
        //act
        LinkResponse act = scrapperClient.removeLink(1L, new RemoveLinkRequest("1"));
        //assert
        Assertions.assertThat(act.id()).isEqualTo(1);
        Assertions.assertThat(act.url()).isEqualTo("1");
    }
}
