package edu.java.scrapper.domain.jdbs;

import edu.java.scrapper.domain.dao.Chat;
import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.domain.service.ChatService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JdbcChatService implements ChatService {
    private final JdbcChatRepository chatRepository;

    @Autowired
    public JdbcChatService(JdbcChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    @Transactional
    public void add(long tgChatId) {
        chatRepository.add(tgChatId);
    }

    @Override
    @Transactional
    public void remove(long tgChatId) {
        chatRepository.remove(tgChatId);
    }

    @Override
    @Transactional
    public Optional<Chat> findById(long apiId) {
        return chatRepository.findById(apiId);
    }
}
