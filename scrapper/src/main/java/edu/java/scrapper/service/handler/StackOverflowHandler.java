package edu.java.scrapper.service.handler;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowHandler implements LinkHandler {
    private final StackOverflowClient stackOverflowClient;
    private final LinkService linkService;

    @Autowired
    public StackOverflowHandler(StackOverflowClient stackOverflowClient, LinkService linkService) {
        this.stackOverflowClient = stackOverflowClient;
        this.linkService = linkService;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public boolean checkLinkForUpdatesAndUpdateIfItNecessary(Link link) {
        String url = link.getUrl();
        String[] partsOfUrl = url.split("/", 6);
        long id = Long.parseLong(partsOfUrl[4]);
        var updateFromSite = stackOverflowClient.getQuestion(id);
        OffsetDateTime updatedTime = updateFromSite.items().get(0).lastEditDate();
        if (updatedTime.isAfter(link.getLastUpdated())) {
            linkService.update(link, updatedTime);
            return true;
        }
        return false;
    }
}
