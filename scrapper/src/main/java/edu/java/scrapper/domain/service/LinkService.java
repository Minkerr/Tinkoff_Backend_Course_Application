package edu.java.scrapper.domain.service;

import edu.java.scrapper.domain.dao.Link;
import java.util.List;

public interface LinkService {
    Link add(long tgChatId, String url);

    Link remove(long tgChatId, String url);

    List<Link> findAll(long tgChatId);
}
