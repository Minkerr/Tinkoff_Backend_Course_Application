CREATE TABLE chat_links
(
    id_chat BIGINT NOT NULL REFERENCES chats (id),
    id_link BIGINT NOT NULL REFERENCES links (id),
    PRIMARY KEY (id_chat, id_link)
);
