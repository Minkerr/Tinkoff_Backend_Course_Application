package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dao.Chat;
import edu.java.scrapper.domain.dao.Link;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
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
        if (linkDontExist){
            String sql = "INSERT INTO links (url, last_update) values (?, ?)";
            jdbcTemplate.update(sql, url, OffsetDateTime.now());
        }
        String sql2 = "INSERT INTO chat_links (id_chat, id_link) values (?, ?)";
        long linkId = findByUrl(url).get().getId();
        long chatId = jdbcChatRepository.findById(chatApiId).get().getId();
        jdbcTemplate.update(sql2, chatId, linkId);
        return findByUrl(url).get();
    }

    public Link remove(long chatApiId, String url) {
        String sql = "DELETE FROM chat_links WHERE id_chat = ? AND id_link = ?";
        long chatId = jdbcChatRepository.findById(chatApiId).get().getId();
        long linkId = findByUrl(url).get().getId();
        jdbcTemplate.update(sql, chatId, linkId);
        return findByUrl(url).get();
    }

    public Optional<Link> findByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Link.class), url).stream().findAny();
    }

    public List<Chat> findUsersWithLink (String url){
        if(findByUrl(url).isEmpty()) return new ArrayList<>();
        long idLink = findByUrl(url).get().getId();
        String sql = "SELECT * FROM chat_links WHERE id_link = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Chat.class), idLink).stream().toList();
    }

    public List<Link> findAll(long apiId){
        long idChat = jdbcChatRepository.findById(apiId).get().getId();
        String sql = "SELECT * FROM chat_links WHERE id_chat = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Link.class), idChat).stream().toList();
    }
}
