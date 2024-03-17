package edu.java.scrapper.domain.service.services;

import edu.java.scrapper.client.GitHubClient;
import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import java.time.OffsetDateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GitHubHandler implements LinkHandler {
    private final GitHubClient gitHubClient;
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public GitHubHandler(GitHubClient gitHubClient, JdbcLinkRepository linkRepository) {
        this.gitHubClient = gitHubClient;
        this.linkRepository = linkRepository;
    }

    @Override
    @SuppressWarnings("MagicNumber")
    public boolean checkLinkForUpdates(Link link) {
        String url = link.getUrl();
        String[] partsOfUrl = url.split("/", 5);
        String owner = partsOfUrl[3];
        String repo = partsOfUrl[4];
        var updateFromSite = gitHubClient.getRepository(owner, repo);
        OffsetDateTime updatedTime = updateFromSite.pushedAt();
        if (updatedTime.isAfter(link.getLastUpdated())) {
            linkRepository.update(link, updatedTime);
            return true;
        }
        return false;
    }
}
