package edu.java.scrapper.service.jdbs;

import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.dto.LinkUpdateRequest;
import edu.java.scrapper.service.LinkService;
import edu.java.scrapper.service.LinkUpdateService;
import edu.java.scrapper.service.handler.GitHubHandler;
import edu.java.scrapper.service.handler.LinkHandler;
import edu.java.scrapper.service.handler.StackOverflowHandler;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdateService implements LinkUpdateService {
    private final long linkCheckPeriodInMinutes = 1;
    private final LinkService linkService;
    private final GitHubHandler gitHubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    @Autowired
    public JdbcLinkUpdateService(
        LinkService linkService,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        this.linkService = linkService;
        this.gitHubHandler = gitHubHandler;
        this.stackOverflowHandler = stackOverflowHandler;
    }

    @Override
    public List<LinkUpdateRequest> update() {
        List<Link> updatedLinks = findLinksUpdatedDuringThePeriod(linkCheckPeriodInMinutes).stream()
            .filter(this::checkLinkForUpdatesAndUpdateIfItNecessary)
            .toList();
        return convertListForRequest(updatedLinks);
    }

    private List<LinkUpdateRequest> convertListForRequest(List<Link> links) {
        return links.stream()
            .map(link -> new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    linkService.findUsersWithLink(link.getUrl()).stream()
                        .map(chat -> chat.getApiId())
                        .toList()
                )
            )
            .toList();
    }

    private List<Link> findLinksUpdatedDuringThePeriod(long timePeriod) {
        var timeBias = OffsetDateTime.now().minusMinutes(timePeriod);
        var allLinksNotUpdated = linkService.findAllLinksUpdatedBefore(timeBias);
        return allLinksNotUpdated;
    }

    private boolean checkLinkForUpdatesAndUpdateIfItNecessary(Link link) {
        String url = link.getUrl();
        LinkHandler handler = null;
        if (url.contains("stackoverflow")) {
            handler = stackOverflowHandler;
        } else if (url.contains("github")) {
            handler = gitHubHandler;
        }
        return handler.checkLinkForUpdatesAndUpdateIfItNecessary(link);
    }
}
