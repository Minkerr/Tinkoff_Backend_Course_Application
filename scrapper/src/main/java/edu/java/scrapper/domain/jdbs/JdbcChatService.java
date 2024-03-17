package edu.java.scrapper.domain.jdbs;

import edu.java.scrapper.domain.repository.JdbcChatRepository;
import edu.java.scrapper.domain.service.ChatService;
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
    public void register(long tgChatId) {
        chatRepository.add(tgChatId);
    }

    @Override
    @Transactional
    public void unregister(long tgChatId) {
        chatRepository.remove(tgChatId);
    }
}
