CREATE TABLE urls(
    id         UUID NOT NULL,
    name       VARCHAR(255),
    created_at TIMESTAMP NOT NULL,
    CONSTRAINT pk_url PRIMARY KEY (id)
);