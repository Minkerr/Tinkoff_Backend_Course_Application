package edu.java.scrapper;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.config.ConfigForRepository;
import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.jdbs.JdbcLinkUpdateService;
import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import edu.java.scrapper.domain.service.services.GitHubHandler;
import edu.java.scrapper.domain.service.services.LinkHandler;
import edu.java.scrapper.domain.service.services.StackOverflowHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.OffsetDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class ServiceTest {
    @Autowired
    JdbcLinkRepository linkRepository;
    @Autowired
    JdbcChatRepository chatRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JdbcLinkUpdateService linkUpdateService;

    @Test
    @Transactional
    @Rollback
    public void linkUpdateService_shouldCheckUpdates() {
        //arrange
        chatRepository.add(1L);
        OffsetDateTime time = OffsetDateTime.now();
        String url1 = "https://github.com/Minkerr/Tinkoff_Backend_Course_Application";
        String url2 = "https://github.com/Minkerr/Tinkoff_Backend_Course";
        String url3 = "https://stackoverflow.com/questions/4105331/how-do-i-convert-from-int-to-string";
        String url4 = "https://stackoverflow.com/questions/5071040";
        Link link1 = new Link(1, url1, time.minusYears(10));
        Link link2 = new Link(2, url2, time);
        Link link3 = new Link(3, url3, time.minusYears(10));
        Link link4 = new Link(4, url4, time);
        //act
        linkRepository.addLink(1L, link1);
        linkRepository.addLink(1L, link2);
        linkRepository.addLink(1L, link3);
        linkRepository.addLink(1L, link4);
        var updatedLinks = linkUpdateService.update();
        //assert
        assertThat(updatedLinks.size()).isEqualTo(2);
        assertThat(updatedLinks.get(0).url()).isEqualTo(url1);
        assertThat(updatedLinks.get(1).url()).isEqualTo(url3);
    }
}
