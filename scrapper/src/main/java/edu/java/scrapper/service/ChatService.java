package edu.java.scrapper.service;

import edu.java.scrapper.domain.model.Chat;
import java.util.Optional;

public interface ChatService {
    void add(long tgChatId);

    void remove(long tgChatId);

    Optional<Chat> findById(long apiId);
}
