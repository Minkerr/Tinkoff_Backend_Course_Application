package edu.java.scrapper.domain.dao;

import java.time.OffsetDateTime;
import java.util.List;

public class Chat {
    long id;
    long apiId;
    OffsetDateTime createdAt;
    List<Link> links;

    public Chat(long id, OffsetDateTime createdAt, List<Link> links) {
        this.id = id;
        this.createdAt = createdAt;
        this.links = links;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(OffsetDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public long getApiId() {
        return apiId;
    }

    public void setApiId(long apiId) {
        this.apiId = apiId;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
