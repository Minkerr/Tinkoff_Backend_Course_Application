package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dao.Chat;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcChatRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcChatRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Chat chat) {
        String sql = "INSERT INTO chats (api_id, created_at) values (?, ?)";
        jdbcTemplate.update(sql, chat.getApiId(), chat.getCreatedAt());
    }

    public void remove(Chat chat) {
        String sql = "DELETE FROM chats WHERE api_id = ?";
        jdbcTemplate.update(sql, chat.getApiId());
    }

    public Optional<Chat> findById(long apiId) {
        String sql = "SELECT * FROM chats WHERE api_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Chat.class), apiId).stream().findAny();
    }
}
