package edu.java.scrapper;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdateService;
import java.time.OffsetDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
@Testcontainers
public class ServiceTest {
    @Autowired
    LinkService linkService;
    @Autowired
    ChatService chatService;
    @Autowired
    LinkUpdateService linkUpdateService;

    @Test
    @Transactional
    @Rollback
    public void linkUpdateService_shouldCheckUpdates() {
        //arrange
        chatService.add(1L);
        chatService.add(2L);
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
        linkService.addLink(1L, link1);
        linkService.addLink(2L, link1);
        linkService.addLink(1L, link2);
        linkService.addLink(1L, link3);
        linkService.addLink(1L, link4);
        var updatedLinks = linkUpdateService.update();
        //assert
        assertThat(updatedLinks.size()).isEqualTo(2);
        assertThat(updatedLinks.get(0).url()).isEqualTo(url1);
        assertThat(updatedLinks.get(1).url()).isEqualTo(url3);
        assertThat(updatedLinks.get(0).tgChatIds().size()).isEqualTo(2);
        assertThat(updatedLinks.get(0).tgChatIds().get(0)).isEqualTo(1L);
        assertThat(updatedLinks.get(0).tgChatIds().get(1)).isEqualTo(2L);

    }
}
