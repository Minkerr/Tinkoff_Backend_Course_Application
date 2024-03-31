package edu.java.scrapper.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "links")
public class LinkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "url")
    @NotNull
    private String url;

    @Column(name = "last_update")
    @NotNull
    private OffsetDateTime lastUpdate;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "chat_links",
               joinColumns = {@JoinColumn(name = "id_link")},
               inverseJoinColumns = {@JoinColumn(name = "id_chat")})
    private List<ChatEntity> chats = new ArrayList<>();

    public LinkEntity() {
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

    public OffsetDateTime getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(OffsetDateTime lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public List<ChatEntity> getChats() {
        return chats;
    }

    public void setChats(List<ChatEntity> chats) {
        this.chats = chats;
    }
}
