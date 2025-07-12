CREATE TABLE message_dlqs (
    id                  BIGSERIAL PRIMARY KEY,

    -- Kafka metadata
    topic               VARCHAR(255) NOT NULL,
    partition           INTEGER      NOT NULL,
    kafka_offset        BIGINT       NOT NULL,
    key                 VARCHAR(255),

    -- Original payload
    value               JSONB        NOT NULL,

    -- Processing context
    trace_id            VARCHAR(255),
    message_type        VARCHAR(100),

    -- Failure info
    error_message       TEXT,
    retry_count         INTEGER      NOT NULL DEFAULT 0,
    status              VARCHAR(32)  NOT NULL DEFAULT 'PENDING', -- PENDING | RETRYING | SUCCESS | FAILED
    last_attempted_at   TIMESTAMPTZ,

    created_at          TIMESTAMPTZ  NOT NULL DEFAULT NOW()
);

COMMENT ON TABLE message_dlqs IS 'Kafka 메시지 처리 실패 시 DLQ 테이블';

CREATE INDEX idx_message_dlqs_topic ON message_dlqs (topic);
CREATE INDEX idx_message_dlqs_trace_id ON message_dlqs (trace_id);
CREATE INDEX idx_message_dlqs_status ON message_dlqs (status);
CREATE INDEX idx_message_dlqs_created_at ON message_dlqs (created_at DESC);
