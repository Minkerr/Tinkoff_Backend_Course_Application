package edu.java.scrapper.service.handler;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubHandler implements LinkHandler {
    private final GitHubClient gitHubClient;
    private final LinkService linkService;

    @Autowired
    public GitHubHandler(GitHubClient gitHubClient, LinkService linkService) {
        this.gitHubClient = gitHubClient;
        this.linkService = linkService;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public boolean checkLinkForUpdatesAndUpdateIfItNecessary(Link link) {
        String url = link.getUrl();
        String[] partsOfUrl = url.split("/", 5);
        String owner = partsOfUrl[3];
        String repo = partsOfUrl[4];
        var updateFromSite = gitHubClient.getRepository(owner, repo);
        OffsetDateTime updatedTime = updateFromSite.pushedAt();
        if (updatedTime.isAfter(link.getLastUpdated())) {
            linkService.update(link, updatedTime);
            return true;
        }
        return false;
    }
}
