package edu.java.scrapper.service.jdbs;

import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.service.ChatService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Autowired
    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void add(long tgChatId) {
        chatRepository.add(tgChatId);
    }

    @Override
    public void remove(long tgChatId) {
        chatRepository.remove(tgChatId);
    }

    @Override
    public Optional<Chat> findById(long apiId) {
        return chatRepository.findById(apiId);
    }
}
