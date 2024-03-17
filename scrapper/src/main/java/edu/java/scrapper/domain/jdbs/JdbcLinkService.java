package edu.java.scrapper.domain.jdbs;

import edu.java.scrapper.domain.dao.Link;
import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.domain.repository.JdbcLinkRepository;
import edu.java.scrapper.domain.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class JdbcLinkService implements LinkService {
    private final JdbcLinkRepository linkRepository;

    @Autowired
    public JdbcLinkService(JdbcLinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    @Override
    public Link add(long tgChatId, String url) {
        return linkRepository.add(tgChatId, url);
    }

    @Override
    public Link remove(long tgChatId, String url) {
        return linkRepository.remove(tgChatId, url);
    }

    @Override
    public List<Link> findAll(long tgChatId) {
        return linkRepository.findAll(tgChatId);
    }
}
