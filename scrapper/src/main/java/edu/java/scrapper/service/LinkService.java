package edu.java.scrapper.service;

import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

public interface LinkService {
    Link add(long tgChatId, String url);

    Link remove(long tgChatId, String url);

    Link addLink(long chatApiId, Link link);

    void update(Link link, OffsetDateTime newLastUpdate);

    Optional<Link> findByUrl(String url);

    List<Chat> findUsersWithLink(String url);

    List<Link> findAllLinksUpdatedBefore(OffsetDateTime timeBias);

    List<Link> findAllLinks(long chatApiId);

}
