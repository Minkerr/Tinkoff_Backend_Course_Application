package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.entity.LinkEntity;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import edu.java.scrapper.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.domain.repository.jpa.JpaLinkRepository;
import edu.java.scrapper.service.LinkService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(propagation = Propagation.REQUIRED)
public class JpaLinkService implements LinkService {
    private final JpaLinkRepository linkRepository;
    private final JpaChatRepository chatRepository;

    @Autowired
    public JpaLinkService(JpaLinkRepository linkRepository, JpaChatRepository chatRepository) {
        this.linkRepository = linkRepository;
        this.chatRepository = chatRepository;
    }

    @Override
    public Link add(long apiId, String url) {
        LinkEntity entity = new LinkEntity();
        entity.setUrl(url);
        entity.setLastUpdate(OffsetDateTime.now());
        entity.getChats().add(chatRepository.findChatEntityByApiId(apiId));
        linkRepository.saveAndFlush(entity);
        var link = linkRepository.findByUrl(url);
        return new Link(link.getId(), link.getUrl());
    }

    @Override
    public Link remove(long tgChatId, String url) {
        linkRepository.deleteByUrl(url);
        var res = findByUrl(url);
        return res.orElseGet(Link::new);
    }

    @Override
    public Link addLink(long chatApiId, Link link) {
        var entity = new LinkEntity();
        entity.setId(link.getId());
        entity.setUrl(link.getUrl());
        entity.setLastUpdate(OffsetDateTime.now());
        entity.getChats().add(chatRepository.findChatEntityByApiId(chatApiId));
        linkRepository.save(entity);
        return link;
    }

    @Override
    public void update(Link link, OffsetDateTime newLastUpdate) {
        linkRepository.updateById(newLastUpdate, link.getId());
    }

    @Override
    public Optional<Link> findByUrl(String url) {
        LinkEntity entity = linkRepository.findByUrl(url);
        Link link = new Link(entity.getId(), url, entity.getLastUpdate());
        return Optional.of(link);
    }

    @Override
    public List<Chat> findUsersWithLink(String url) {
        return linkRepository.findChatsByLink(url).stream()
            .map(chatEntity -> new Chat(
                chatEntity.getId(),
                chatEntity.getApiId(),
                chatEntity.getCreatedAt(),
                chatEntity.getLinks().stream()
                    .map(linkEntity -> new Link(
                        linkEntity.getId(),
                        linkEntity.getUrl(),
                        linkEntity.getLastUpdate()
                    ))
                    .toList()
            ))
            .toList();
    }

    @Override
    public List<Link> findAllLinksUpdatedBefore(OffsetDateTime timeBias) {
        return linkRepository.findLinkEntitiesByLastUpdateBefore(timeBias).stream()
            .map(linkEntity -> new Link(
                linkEntity.getId(),
                linkEntity.getUrl(),
                linkEntity.getLastUpdate()
            ))
            .toList();
    }

    @Override
    public List<Link> findAllLinks(long chatApiId) {
        return linkRepository.findAllByApiId(chatApiId).stream()
            .map(linkEntity -> new Link(
                linkEntity.getId(),
                linkEntity.getUrl(),
                linkEntity.getLastUpdate()
            ))
            .toList();
    }
}
