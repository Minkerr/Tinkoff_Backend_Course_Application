package edu.java.scrapper;

import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class RepositoriesTest {
    @Autowired
    private LinkService linkService;
    @Autowired
    private ChatService chatService;

    @Test
    @Transactional
    @Rollback
    public void chatRepository_shouldAddDeleteAndFoundChatsInDB() {
        //arrange
        //act
        chatService.add(1L);
        chatService.add(2L);
        chatService.add(3L);
        chatService.remove(2L);
        //assert
        assertThat(chatService.findById(1L).get().getApiId()).isEqualTo(1L);
        assertThat(chatService.findById(2L)).isEmpty();
        assertThat(chatService.findById(3L).get().getApiId()).isEqualTo(3L);
    }

    @Test
    @Transactional
    @Rollback
    public void linkRepository_shouldAddDeleteAndFoundLinksInDB() {
        //arrange
        chatService.add(1L);
        //act
        linkService.add(1L, "url1");
        linkService.add(1L, "url2");
        linkService.add(1L, "url3");
        linkService.remove(1L, "url2");
        //assert
        assertThat(linkService.findByUrl("url1").get().getUrl()).isEqualTo("url1");
        assertThat(linkService.findUsersWithLink("url2").size()).isEqualTo(0);
        assertThat(linkService.findUsersWithLink("url3").get(0).getApiId()).isEqualTo(1L);
        assertThat(linkService.findByUrl("url3").get().getUrl()).isEqualTo("url3");
    }
}
