package edu.java.scrapper.configuration;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClientConfiguration {
    @Value("${api.github.base-url}")
    private String gitHubBaseUrl;

    @Value("${api.stackoverflow.base-url}")
    private String stackOverflowBaseUrl;

    @Value("${api.bot.base-url}")
    private String botBaseUrl;

    @Bean
    public StackOverflowClient stackOverflowClient() {
        return new StackOverflowClient(stackOverflowBaseUrl);
    }

    @Bean
    public GitHubClient gitHubWebClient() {
        return new GitHubClient(gitHubBaseUrl);
    }

    @Bean
    public BotClient botClient() {
        return new BotClient(botBaseUrl);
    }
}
