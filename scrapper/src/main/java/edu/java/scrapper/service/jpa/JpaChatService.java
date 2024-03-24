package edu.java.scrapper.service.jpa;

import edu.java.scrapper.domain.entity.ChatEntity;
import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.repository.jpa.JpaChatRepository;
import edu.java.scrapper.service.ChatService;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JpaChatService implements ChatService {
    private final JpaChatRepository chatRepository;

    @Autowired
    public JpaChatService(JpaChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    @Override
    public void add(long tgChatId) {
        ChatEntity chat = new ChatEntity();
        chat.setCreatedAt(OffsetDateTime.now());
        chat.setId(tgChatId);
        chatRepository.save(chat);
    }

    @Override
    public void remove(long tgChatId) {
        chatRepository.deleteChatEntityById(tgChatId);
    }

    @Override
    public Optional<Chat> findById(long apiId) {
        ChatEntity entity = chatRepository.findChatEntityByApiId(apiId);
        if (entity != null) {
            return Optional.of(new Chat(entity.getId(), entity.getApiId(), OffsetDateTime.now(), List.of()));
        }else {
            return Optional.empty();
        }
    }
}
