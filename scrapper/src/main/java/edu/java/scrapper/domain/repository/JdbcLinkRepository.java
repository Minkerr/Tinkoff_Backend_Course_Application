package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.model.Chat;
import edu.java.scrapper.domain.model.Link;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@SuppressWarnings("MultipleStringLiterals")
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;
    private final JdbcChatRepository jdbcChatRepository;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate, JdbcChatRepository jdbcChatRepository) {
        this.jdbcTemplate = jdbcTemplate;
        this.jdbcChatRepository = jdbcChatRepository;
    }

    public Link add(long chatApiId, String url) {
        var linkDontExist = findByUrl(url).isEmpty();
        if (linkDontExist) {
            String sql = "INSERT INTO links (url, last_update) values (?, ?)";
            jdbcTemplate.update(sql, url, OffsetDateTime.now());
        }
        String sqlForSecondTable = "INSERT INTO chat_links (id_chat, id_link) values (?, ?)";
        long linkId = findByUrl(url).get().getId();
        long chatId = jdbcChatRepository.findById(chatApiId).get().getId();
        jdbcTemplate.update(sqlForSecondTable, chatId, linkId);
        return findByUrl(url).get();
    }

    public Link addLink(long chatApiId, Link link) {
        String sql = "INSERT INTO links (url, last_update) values (?, ?) ON CONFLICT (url) DO NOTHING";
        jdbcTemplate.update(sql, link.getUrl(), link.getLastUpdated());
        String sqlForSecondTable = "INSERT INTO chat_links (id_chat, id_link) values (?, ?)";
        long chatId = jdbcChatRepository.findById(chatApiId).get().getId();
        String sqlForFindingLinks = "SELECT * FROM links WHERE url = ? AND last_update = ?";
        long linkId = jdbcTemplate.query(
                sqlForFindingLinks,
                (rs, rowNum) -> new Link(
                    rs.getLong("id"),
                    rs.getString("url"),
                    rs.getTimestamp("last_update").toInstant().atOffset(ZoneOffset.UTC)
                ),
                link.getUrl(),
                link.getLastUpdated()
            )
            .get(0).getId();
        jdbcTemplate.update(sqlForSecondTable, chatId, linkId);
        return link;
    }

    public Link remove(long chatApiId, String url) {
        String sql = "DELETE FROM chat_links WHERE id_chat = ? AND id_link = ?";
        long chatId = jdbcChatRepository.findById(chatApiId).get().getId();
        var optionalLink = findByUrl(url);
        if (optionalLink.isEmpty()){
            return new Link();
        }
        long linkId = optionalLink.get().getId();
        jdbcTemplate.update(sql, chatId, linkId);
        return optionalLink.get();
    }

    public void update(Link link, OffsetDateTime newLastUpdate) {
        jdbcTemplate.update("UPDATE links SET last_update = ? WHERE id = ?", newLastUpdate, link.getId());
    }

    public Optional<Link> findByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        return jdbcTemplate.query(sql, (rs, rowNum) -> new Link(
                rs.getLong("id"),
                rs.getString("url"),
                rs.getTimestamp("last_update").toInstant().atOffset(ZoneOffset.UTC)
            ),
            url
        ).stream().findAny();
    }

    public List<Chat> findUsersWithLink(String url) {
        if (findByUrl(url).isEmpty()) {
            return new ArrayList<>();
        }
        long idLink = findByUrl(url).get().getId();
        String sql = "SELECT * FROM chats WHERE id IN (SELECT id_chat FROM chat_links WHERE id_link = ?)";
        return jdbcTemplate.query(
            sql,
            (r, row) -> new Chat(
                r.getLong("id"),
                r.getLong("api_id"),
                r.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
                new ArrayList<>()
            ),
            idLink
        );
    }

    public List<Link> findAllLinksUpdatedBefore(OffsetDateTime timeBias) {
        String sql = "SELECT * FROM links WHERE last_update < ?";
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Link(
                rs.getLong("id"),
                rs.getString("url"),
                rs.getTimestamp("last_update").toInstant().atOffset(ZoneOffset.UTC)
            ),
            timeBias
        ).stream().toList();
    }

    public List<Link> findAllLinks(long chatApiId) {
        String sql = """
            SELECT * FROM links
            WHERE id IN
            (SELECT id_link FROM chat_links
            WHERE id_chat IN
            (SELECT id FROM chats
            WHERE api_id = ?))
            """;
        return jdbcTemplate.query(
            sql,
            (rs, rowNum) -> new Link(
                rs.getLong("id"),
                rs.getString("url"),
                rs.getTimestamp("last_update").toInstant().atOffset(ZoneOffset.UTC)
            ),
            chatApiId
        ).stream().toList();
    }
}
