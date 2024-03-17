package edu.java.scrapper.domain.repository;

import edu.java.scrapper.domain.dao.Link;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JdbcLinkRepository {
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcLinkRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void add(Link link) {
        String sql = "INSERT INTO links (url, last_update) values (?, ?)";
        jdbcTemplate.update(sql, link.getUrl(), link.getLastUpdated());
    }

    public void remove(Link link) {
        String sql = "DELETE FROM links WHERE url = ?";
        jdbcTemplate.update(sql, link.getUrl());
    }

    public Link findByUrl(String url) {
        String sql = "SELECT * FROM links WHERE url = ?";
        return jdbcTemplate.query(sql, new BeanPropertyRowMapper<>(Link.class), url).get(0);
    }
}
