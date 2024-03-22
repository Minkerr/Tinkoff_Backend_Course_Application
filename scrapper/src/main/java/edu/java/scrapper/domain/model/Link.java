package edu.java.scrapper.domain.model;

import java.time.OffsetDateTime;

public class Link {
    long id;
    String url;
    OffsetDateTime lastUpdated;

    public Link() {
    }

    public Link(long id, String url, OffsetDateTime lastUpdated) {
        this.id = id;
        this.url = url;
        this.lastUpdated = lastUpdated;
    }

    public Link(long id, String url) {
        this.id = id;
        this.url = url;
        this.lastUpdated = OffsetDateTime.now();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public OffsetDateTime getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(OffsetDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
}
