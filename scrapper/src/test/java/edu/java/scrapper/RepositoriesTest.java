package edu.java.scrapper;

import edu.java.scrapper.config.ConfigForRepository;
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

@SpringBootTest
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
        //act
        chatRepository.add(1L);
        chatRepository.add(2L);
        chatRepository.add(3L);
        chatRepository.remove(2L);
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
        chatRepository.add(1L);
        //act
        linkRepository.add(1L, "url1");
        linkRepository.add(1L, "url2");
        linkRepository.add(1L, "url3");
        linkRepository.remove(1L, "url2");
        //assert
        assertThat(linkRepository.findByUrl("url1").get().getUrl()).isEqualTo("url1");
        assertThat(linkRepository.findUsersWithLink("url2").size()).isEqualTo(0);
        assertThat(linkRepository.findByUrl("url3").get().getUrl()).isEqualTo("url3");
        assertThat(linkRepository.findByUrl("url3")).isNotEmpty();
        assertThat(linkRepository.findAll(1L).size()).isEqualTo(2);

    }
}
