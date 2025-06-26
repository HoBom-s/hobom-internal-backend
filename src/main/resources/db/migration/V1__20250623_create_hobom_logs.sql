CREATE TABLE hobom_logs (
    id            BIGSERIAL PRIMARY KEY,
    service_type  VARCHAR(50)  NOT NULL,
    level         VARCHAR(50)  NOT NULL,
    trace_id      VARCHAR(255) NOT NULL,
    message       TEXT         NOT NULL,
    http_method   VARCHAR(20)  NOT NULL,
    path          TEXT,
    status_code   INTEGER      NOT NULL,
    host          VARCHAR(255) NOT NULL,
    user_id       VARCHAR(255) NOT NULL,
    payload       JSONB,
    timestamp     TIMESTAMP(3) NOT NULL,
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at
BEFORE UPDATE ON hobom_logs
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();