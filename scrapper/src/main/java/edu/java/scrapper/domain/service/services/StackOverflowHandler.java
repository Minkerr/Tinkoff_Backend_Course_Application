package edu.java.scrapper.domain.service.services;

import edu.java.scrapper.client.StackOverflowClient;
import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StackOverflowHandler implements LinkHandler {
    private final StackOverflowClient stackOverflowClient;
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public StackOverflowHandler(StackOverflowClient stackOverflowClient, JdbcLinkRepository linkRepository) {
        this.stackOverflowClient = stackOverflowClient;
        this.linkRepository = linkRepository;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public boolean checkLinkForUpdates(Link link) {
        String url = link.getUrl();
        String[] partsOfUrl = url.split("/", 5);
        long id = Long.parseLong(partsOfUrl[4]);
        var updateFromSite = stackOverflowClient.getQuestion(id);
        OffsetDateTime updatedTime = updateFromSite.items().get(0).lastEditDate();
        if (updatedTime.isAfter(link.getLastUpdated())) {
            linkRepository.update(link, updatedTime);
            return true;
        }
        return false;
    }
}
