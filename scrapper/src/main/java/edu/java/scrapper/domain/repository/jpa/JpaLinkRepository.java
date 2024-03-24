package edu.java.scrapper.domain.repository.jpa;

import edu.java.scrapper.domain.entity.ChatEntity;
import edu.java.scrapper.domain.entity.LinkEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;
import java.util.List;

@Repository
public interface JpaLinkRepository extends JpaRepository<LinkEntity, Long> {
    void deleteByUrl(String url);

    LinkEntity findByUrl(String url);

    List<LinkEntity> findLinkEntitiesByLastUpdateBefore(OffsetDateTime lastUpdated);

    void updateById(long id, OffsetDateTime time);

    @Query(value = "SELECT id, api_id FROM chats WHERE id IN (SELECT id_chat FROM chat_links WHERE id_link = ?)",
           nativeQuery = true)
    List<ChatEntity> findChatsByLink(String url);

    @Query(value = """
            SELECT * FROM links
            WHERE id IN
            (SELECT id_link FROM chat_links
            WHERE id_chat IN
            (SELECT id FROM chats
            WHERE api_id = ?))
            """, nativeQuery = true)
    List<LinkEntity> findAllByApiId(long apiId);
}
