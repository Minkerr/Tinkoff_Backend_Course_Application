CREATE TABLE chats
(
    id         BIGINT GENERATED ALWAYS AS IDENTITY,
    api_id     BIGINT NOT NULL,
    created_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY(id)
);


