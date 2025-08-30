CREATE TYPE approval_status AS ENUM ('DRAFT','PENDING','IN_REVIEW','APPROVED','REJECTED','CANCELED','EXPIRED');
CREATE TYPE stage_status    AS ENUM ('PENDING','APPROVED','REJECTED','SKIPPED');
CREATE TYPE step_mode       AS ENUM ('ALL','ANY');
CREATE TYPE step_status     AS ENUM ('PENDING','APPROVED','REJECTED','SKIPPED');

-- Main 테이블
CREATE TABLE approval_request(
  id               BIGSERIAL        PRIMARY KEY,
  tenant_id        TEXT             NOT NULL,
  requester_id     TEXT             NOT NULL,
  title            TEXT             NOT NULL,
  -- 원본 참조 + 스냅샷
  resource_type    TEXT,
  resource_id      TEXT,
  resource_ver     INT,
  context_snapshot JSONB            NOT NULL DEFAULT '{}'::jsonb,
  context_hash     TEXT,
  template_id      TEXT,
  -- 상태/메타
  status           approval_status  NOT NULL,
  version          INT              NOT NULL DEFAULT 0,
  created_at       TIMESTAMPTZ      NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ      NOT NULL DEFAULT now()
);
CREATE INDEX ix_approval_status     ON approval_request(status);
CREATE INDEX ix_approval_tenant     ON approval_request(tenant_id);
CREATE INDEX ix_approval_requester  ON approval_request(requester_id);

-- 차수(Stage): 한 "n차"를 의미
CREATE TABLE approval_stage(
  id            BIGSERIAL           PRIMARY KEY,
  approval_id   TEXT                NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  stage_order   INT                 NOT NULL,
  mode          step_mode           NOT NULL,
  status        stage_status        NOT NULL DEFAULT 'PENDING',
  decided_by    TEXT,
  decided_at    TIMESTAMPTZ,
  comment       TEXT
);
CREATE UNIQUE INDEX ux_stage        ON approval_stage(approval_id, stage_order);
CREATE INDEX ix_stage_status        ON approval_stage(status);

-- 스텝(Step): 차수 내 병렬 승인 단위
CREATE TABLE approval_step(
  id         BIGSERIAL              PRIMARY KEY,
  stage_id   BIGINT                 NOT NULL REFERENCES approval_stage(id) ON DELETE CASCADE,
  status     step_status            NOT NULL DEFAULT 'PENDING',
  decided_by TEXT,
  decided_at TIMESTAMPTZ,
  comment    TEXT
);

-- 스텝 승인자(개인 or 역할)
CREATE TABLE approval_step_approver(
  id      BIGSERIAL                 PRIMARY KEY,
  step_id BIGINT                    NOT NULL REFERENCES approval_step(id) ON DELETE CASCADE,
  user_id TEXT,
  role    TEXT,
  CHECK (user_id IS NOT NULL OR role IS NOT NULL)
);

-- 감사 로그
CREATE TABLE approval_action(
  id           BIGSERIAL            PRIMARY KEY,
  approval_id  TEXT                 NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  stage_id     BIGINT               NULL REFERENCES approval_stage(id) ON DELETE SET NULL,
  step_id      BIGINT               NULL REFERENCES approval_step(id) ON DELETE SET NULL,
  actor_id     TEXT,
  type         TEXT                 NOT NULL,
  comment      TEXT,
  at           TIMESTAMPTZ          NOT NULL DEFAULT now()
);
CREATE INDEX ix_action_approval     ON approval_action(approval_id);

-- 콜백(웹훅) 재시도 관리
CREATE TABLE approval_callback(
  id             BIGSERIAL          PRIMARY KEY,
  approval_id    TEXT               NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  target_service TEXT               NOT NULL,
  endpoint       TEXT               NOT NULL,
  signature_key  TEXT               NOT NULL,
  headers        JSONB              NOT NULL DEFAULT '{}'::jsonb,
  state          TEXT               NOT NULL,
  retry_count    INT                NOT NULL DEFAULT 0,
  last_error     TEXT,
  next_retry_at  TIMESTAMPTZ
);
CREATE INDEX ix_cb_state_due        ON approval_callback(state, next_retry_at);

-- 멱등키
CREATE TABLE idempotency_keys(
  scope      TEXT                   NOT NULL, -- create_approval / decision / callback
  key        TEXT                   NOT NULL,
  created_at TIMESTAMPTZ            NOT NULL DEFAULT now(),
  PRIMARY KEY(scope, key)
);

-- Outbox: SSE/Kafka 등 브로드캐스트용
CREATE TABLE approval_outbox(
  id           BIGSERIAL            PRIMARY KEY,
  approval_id  TEXT                 NOT NULL,
  event_type   TEXT                 NOT NULL,
  payload      JSONB                NOT NULL,
  created_at   TIMESTAMPTZ          NOT NULL DEFAULT now(),
  published_at TIMESTAMPTZ
);
CREATE INDEX ix_outbox_unpublished  ON approval_outbox(published_at);
