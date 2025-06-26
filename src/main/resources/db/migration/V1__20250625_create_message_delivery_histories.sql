CREATE TABLE message_delivery_histories (
    id          BIGSERIAL PRIMARY KEY,
    type        VARCHAR(32)   NOT NULL,
    title       VARCHAR(255)  NOT NULL,
    body        TEXT          NOT NULL,
    recipient   VARCHAR(255)  NOT NULL,
    sender_id   VARCHAR(128),
    sent_at     TIMESTAMP     NOT NULL,
    created_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at  TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
   NEW.updated_at = CURRENT_TIMESTAMP;
   RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_updated_at_on_message_delivery
BEFORE UPDATE ON message_delivery_histories
FOR EACH ROW
EXECUTE FUNCTION update_updated_at_column();
