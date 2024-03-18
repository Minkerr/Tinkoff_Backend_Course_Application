package edu.java.scrapper.domain.service;

import edu.java.scrapper.domain.dao.Chat;
import java.util.Optional;

public interface ChatService {
    void add(long tgChatId);

    void remove(long tgChatId);

    Optional<Chat> findById(long apiId);
}
