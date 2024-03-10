package edu.java.bot;

import com.github.tomakehurst.wiremock.WireMockServer;
import edu.java.bot.client.ScrapperClient;
import edu.java.bot.dto.LinkUpdate;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.List;

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
    public void scrapperClient_shouldProcessRequests(){
        //arrange

        //act

        //assert

    }
}
