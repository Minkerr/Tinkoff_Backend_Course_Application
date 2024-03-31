package edu.java.scrapper.service.jdbs;

import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.domain.repository.jdbc.JdbcLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    @Transactional
    public Link add(long tgChatId, String url) {
        return linkRepository.add(tgChatId, url);
    }

    @Override
    @Transactional
    public Link remove(long tgChatId, String url) {
        return linkRepository.remove(tgChatId, url);
    }

    @Override
    @Transactional
    public Link addLink(long chatApiId, Link link) {
        return linkRepository.addLink(chatApiId, link);
    }

    @Override
    public void update(Link link, OffsetDateTime newLastUpdate) {
        linkRepository.update(link, newLastUpdate);
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        return linkRepository.findByUrl(url);
    }

    @Override
    public List<Chat> findUsersWithLink(String url) {
        return linkRepository.findUsersWithLink(url);
    }

    @Override
    public List<Link> findAllLinks(long chatApiId) {
        return linkRepository.findAllLinks(chatApiId);
    }

    @Override
    public List<Link> findAllLinksUpdatedBefore(OffsetDateTime timeBias) {
        return linkRepository.findAllLinksUpdatedBefore(timeBias);
    }
}
