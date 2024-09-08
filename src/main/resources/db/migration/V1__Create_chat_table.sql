create table CHAT
(
    id SERIAL PRIMARY KEY,
    FROM_USER BIGINT not null,
    TO_USER BIGINT not null,
    MESSAGE TEXT not null,
    DATE TIMESTAMP,
    unique_chat VARCHAR(50),
    message_number BIGINT DEFAULT 1
);