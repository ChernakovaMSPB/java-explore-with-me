create TABLE IF NOT EXISTS categories
(
    id   BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

create TABLE IF NOT EXISTS users
(
    id    BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name  VARCHAR(512) NOT NULL,
    email VARCHAR(512) NOT NULL,
    CONSTRAINT UQ_USER_EMAIL UNIQUE (email)
);

create TABLE IF NOT EXISTS locations
(
    id  BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    lat FLOAT,
    lon FLOAT
);

create TABLE IF NOT EXISTS events
(
    id                 BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    annotation         VARCHAR(2000) NOT NULL,
    category_id        BIGINT,
    confirmed_requests BIGINT,
    created_on         TIMESTAMP WITHOUT TIME ZONE,
    description        VARCHAR(7000) NOT NULL,
    event_date         TIMESTAMP WITHOUT TIME ZONE,
    initiator          BIGINT,
    location_id        BIGINT,
    paid               BOOLEAN,
    participant_limit  BIGINT,
    published_on       TIMESTAMP WITHOUT TIME ZONE,
    request_moderation BOOLEAN,
    state              VARCHAR(120),
    title              VARCHAR(120),
    views              BIGINT,
    FOREIGN KEY (location_id) REFERENCES locations (id),
    FOREIGN KEY (category_id) REFERENCES categories (id),
    FOREIGN KEY (initiator) REFERENCES users (id)
);

create TABLE IF NOT EXISTS compilations
(
    id     BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    title  VARCHAR(2048) NOT NULL,
    pinned BOOLEAN,
    events BIGINT,
    FOREIGN KEY (events) REFERENCES events (id),
    CONSTRAINT UQ_COMPILATION_NAME UNIQUE (title)
);

create TABLE IF NOT EXISTS requests
(
    id           BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    created      TIMESTAMP WITHOUT TIME ZONE,
    event_id     BIGINT,
    status       VARCHAR(64),
    requester_id BIGINT,
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (requester_id) REFERENCES users (id)
);

create TABLE IF NOT EXISTS event_compilations
(
    event_id       BIGINT,
    compilation_id BIGINT,
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (compilation_id) REFERENCES compilations (id)
);

create TABLE IF NOT EXISTS comments
(
    id BIGINT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    event_id BIGINT,
    author_id BIGINT,
    text VARCHAR(512),
    created TIMESTAMP WITHOUT TIME ZONE,
    FOREIGN KEY (event_id) REFERENCES events (id),
    FOREIGN KEY (author_id) REFERENCES users (id)
);
