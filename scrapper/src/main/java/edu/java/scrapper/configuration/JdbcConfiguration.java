package edu.java.scrapper.configuration;

import edu.java.scrapper.domain.repository.jdbc.JdbcChatRepository;
import edu.java.scrapper.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.jdbs.JdbcChatService;
import edu.java.scrapper.service.jdbs.JdbcLinkService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(prefix = "app", name = "database-access-type", havingValue = "jdbc")
public class JdbcConfiguration {
    @Bean
    public LinkService linkService(JdbcLinkRepository linkRepository) {
        return new JdbcLinkService(linkRepository);
    }

    @Bean
    public ChatService chatService(JdbcChatRepository chatRepository) {
        return new JdbcChatService(chatRepository);
    }
}
