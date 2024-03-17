package edu.java.scrapper.domain.jdbs;

import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import edu.java.scrapper.domain.service.LinkUpdateService;
import edu.java.scrapper.domain.service.services.GitHubHandler;
import edu.java.scrapper.domain.service.services.LinkHandler;
import edu.java.scrapper.domain.service.services.StackOverflowHandler;
import edu.java.scrapper.dto.LinkUpdateRequest;
import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcLinkUpdateService implements LinkUpdateService {
    private final long linkCheckPeriodInMinutes = 1;
    private final JdbcLinkRepository linkRepository;
    private final GitHubHandler gitHubHandler;
    private final StackOverflowHandler stackOverflowHandler;

    @Autowired
    public JdbcLinkUpdateService(
        JdbcLinkRepository linkRepository,
        GitHubHandler gitHubHandler,
        StackOverflowHandler stackOverflowHandler
    ) {
        this.linkRepository = linkRepository;
        this.gitHubHandler = gitHubHandler;
        this.stackOverflowHandler = stackOverflowHandler;
    }

    @Override
    public List<LinkUpdateRequest> update() {
        List<Link> updatedLinks = findLinksUpdatedDuringThePeriod(linkCheckPeriodInMinutes).stream()
            .filter(this::checkLinkForUpdates)
            .toList();
        return convertListForRequest(updatedLinks);
    }

    private List<LinkUpdateRequest> convertListForRequest(List<Link> links) {
        return links.stream()
            .map(link -> new LinkUpdateRequest(
                    link.getId(),
                    link.getUrl(),
                    linkRepository.findUsersWithLink(link.getUrl()).stream()
                        .map(chat -> chat.getApiId())
                        .toList()
                )
            )
            .toList();
    }

    private List<Link> findLinksUpdatedDuringThePeriod(long timePeriod) {
        var timeBias = OffsetDateTime.now().minusMinutes(timePeriod);
        var allLinksNotUpdated = linkRepository.findAllLinksUpdatedBefore(timeBias);
        return allLinksNotUpdated;
    }

    private boolean checkLinkForUpdates(Link link) {
        String url = link.getUrl();
        LinkHandler handler = null;
        if (url.contains("stackoverflow")) {
            handler = stackOverflowHandler;
        } else if (url.contains("github")) {
            handler = gitHubHandler;
        }
        return handler.checkLinkForUpdates(link);
    }
}
