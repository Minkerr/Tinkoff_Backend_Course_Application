package edu.java.scrapper;

import edu.java.scrapper.client.BotClient;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.dto.LinkUpdateRequest;
import edu.java.scrapper.service.ChatService;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdateService;
import edu.java.scrapper.service.jdbs.JdbcLinkUpdateService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.OffsetDateTime;
import java.util.List;

@EnableScheduling
@Component
public class LinkUpdaterScheduler {
    private static final Logger LOGGER = LogManager.getLogger();
    private final LinkUpdateService updateService;
    private final BotClient botClient;
    @Autowired
    public LinkUpdaterScheduler(JdbcLinkUpdateService updateService, BotClient botClient) {
        this.updateService = updateService;
        this.botClient = botClient;
    }

    @Scheduled(fixedDelayString = "${app.scheduler.interval}")
    public void update() {
        LOGGER.info("Changes have occurred");
        List<LinkUpdateRequest> linkUpdateRequests = updateService.update();

        for (var request : linkUpdateRequests) {
            var response = botClient.sendUpdate(request);
            LOGGER.info("updates in " + response);
        }
    }
}
