CREATE TABLE hobom_logs (
    id            BIGINT       AUTO_INCREMENT PRIMARY KEY,
    service_type  VARCHAR(50)  NOT NULL,
    level         VARCHAR(50)  NOT NULL,
    trace_id      VARCHAR(255) NOT NULL,
    message       TEXT         NOT NULL,
    http_method   VARCHAR(20)  NOT NULL,
    path          TEXT,
    status_code   INT          NOT NULL,
    host          VARCHAR(255) NOT NULL,
    user_id       VARCHAR(255) NOT NULL,
    payload       JSON,
    timestamp     TIMESTAMP(3) NOT NULL,
    created_at    DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP,
    updated_at    DATETIME     NOT NULL  DEFAULT CURRENT_TIMESTAMP
                               ON UPDATE CURRENT_TIMESTAMP
);
