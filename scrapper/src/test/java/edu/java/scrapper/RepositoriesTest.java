package edu.java.scrapper;

import edu.java.scrapper.config.ConfigForRepository;
import edu.java.scrapper.domain.dao.Chat;
import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import java.time.OffsetDateTime;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest(classes = {ConfigForRepository.class, JdbcChatRepository.class, JdbcLinkRepository.class})
@Testcontainers
public class RepositoriesTest {
    @Autowired
    JdbcLinkRepository linkRepository;
    @Autowired
    JdbcChatRepository chatRepository;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Test
    @Transactional
    @Rollback
    public void chatRepository_shouldAddDeleteAndFoundChatsInDB() {
        //arrange
        OffsetDateTime date = OffsetDateTime.now();
        Chat chat1 = new Chat(1L, date);
        Chat chat2 = new Chat(2L, date);
        Chat chat3 = new Chat(3L, date);
        //act
        chatRepository.add(chat1);
        chatRepository.add(chat2);
        chatRepository.add(chat3);
        chatRepository.remove(chat2);
        //assert
        assertThat(chatRepository.findById(1L).get().getApiId()).isEqualTo(1L);
        assertThat(chatRepository.findById(2L)).isEmpty();
        assertThat(chatRepository.findById(3L).get().getApiId()).isEqualTo(3L);

    }

    @Test
    @Transactional
    @Rollback
    public void linkRepository_shouldAddDeleteAndFoundLinksInDB() {
        //arrange
        OffsetDateTime date = OffsetDateTime.now();
        Link link1 = new Link("url1", date);
        Link link2 = new Link("url2", date);
        Link link3 = new Link("url3", date);
        //act
        linkRepository.add(link1);
        linkRepository.add(link2);
        linkRepository.add(link3);
        linkRepository.remove(link2);
        //assert
        assertThat(linkRepository.findByUrl("url1").get().getUrl()).isEqualTo("url1");
        assertThat(linkRepository.findByUrl("url2")).isEmpty();
        assertThat(linkRepository.findByUrl("url3").get().getUrl()).isEqualTo("url3");

    }
}
