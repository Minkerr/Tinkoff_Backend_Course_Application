package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.model.Chat;
import java.time.OffsetDateTime;
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

    public void add(long apiId) {
        String sql = "INSERT INTO chats (api_id, created_at) values (?, ?)";
        jdbcTemplate.update(sql, apiId, OffsetDateTime.now());
    }

    public void remove(long apiId) {
        String sql = "DELETE FROM chats WHERE api_id = ?";
        jdbcTemplate.update(sql, apiId);
    }

    public Optional<Chat> findById(long apiId) {
        String sql = "SELECT * FROM chats WHERE api_id = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Chat.class), apiId).stream().findAny();
    }
}
