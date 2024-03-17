package edu.java.scrapper.domain.service;

public interface ChatService {
    void register(long tgChatId);

    void unregister(long tgChatId);
}
