CREATE TYPE approval_status AS ENUM ('DRAFT','PENDING','IN_REVIEW','APPROVED','REJECTED','CANCELED','EXPIRED');
CREATE TYPE stage_status    AS ENUM ('PENDING','APPROVED','REJECTED','SKIPPED');
CREATE TYPE step_mode       AS ENUM ('ALL','ANY');
CREATE TYPE step_status     AS ENUM ('PENDING','APPROVED','REJECTED','SKIPPED');

-- Main
CREATE TABLE approval_request(
  id               BIGSERIAL        PRIMARY KEY,
  tenant_id        VARCHAR(255)     NOT NULL,
  requester_id     VARCHAR(255)     NOT NULL,
  title            TEXT             NOT NULL,
  resource_type    TEXT,
  resource_id      TEXT,
  resource_ver     INT,
  context_snapshot JSONB            NOT NULL DEFAULT '{}'::jsonb,
  context_hash     TEXT,
  template_id      TEXT,
  status           approval_status  NOT NULL,
  version          INT              NOT NULL DEFAULT 0,
  created_at       TIMESTAMPTZ      NOT NULL DEFAULT now(),
  updated_at       TIMESTAMPTZ      NOT NULL DEFAULT now(),
  CONSTRAINT chk_resource_pair CHECK (
    (resource_type IS NULL AND resource_id IS NULL)
    OR (resource_type IS NOT NULL AND resource_id IS NOT NULL)
  )
);
CREATE INDEX ix_approval_status     ON approval_request(status);
CREATE INDEX ix_approval_tenant     ON approval_request(tenant_id);
CREATE INDEX ix_approval_requester  ON approval_request(requester_id);

-- Stage (n차)
CREATE TABLE approval_stage(
  id            BIGSERIAL           PRIMARY KEY,
  approval_id   BIGINT              NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  stage_order   INT                 NOT NULL,
  mode          step_mode           NOT NULL,
  status        stage_status        NOT NULL DEFAULT 'PENDING',
  decided_by    TEXT,
  decided_at    TIMESTAMPTZ,
  comment       TEXT
);
CREATE UNIQUE INDEX ux_stage        ON approval_stage(approval_id, stage_order);
CREATE INDEX ix_stage_status        ON approval_stage(status);

-- Step (차수 내 병렬 단위)
CREATE TABLE approval_step(
  id         BIGSERIAL              PRIMARY KEY,
  stage_id   BIGINT                 NOT NULL REFERENCES approval_stage(id) ON DELETE CASCADE,
  status     step_status            NOT NULL DEFAULT 'PENDING',
  decided_by TEXT,
  decided_at TIMESTAMPTZ,
  comment    TEXT
);

-- Step approver
CREATE TABLE approval_step_approver(
  id      BIGSERIAL                 PRIMARY KEY,
  step_id BIGINT                    NOT NULL REFERENCES approval_step(id) ON DELETE CASCADE,
  user_id VARCHAR(255),
  role    VARCHAR(255),
  CHECK (user_id IS NOT NULL OR role IS NOT NULL)
);

-- Audit Log
CREATE TABLE approval_action(
  id               BIGSERIAL        PRIMARY KEY,
  approval_id      BIGINT           NOT NULL REFERENCES approval_request(id) ON DELETE RESTRICT,
  stage_id         BIGINT           NULL REFERENCES approval_stage(id) ON DELETE SET NULL,
  step_id          BIGINT           NULL REFERENCES approval_step(id)  ON DELETE SET NULL,
  actor_id         VARCHAR(255),
  type             VARCHAR(64)      NOT NULL, -- REQUEST/APPROVE/REJECT/COMMENT/CANCEL ...
  comment          TEXT,
  at               TIMESTAMPTZ      NOT NULL DEFAULT now(),
  idempotency_key  VARCHAR(255)     NOT NULL
);
CREATE INDEX ix_action_approval     ON approval_action(approval_id, at);
CREATE INDEX ix_action_actor        ON approval_action(actor_id, at);
CREATE UNIQUE INDEX ux_action_idem  ON approval_action(idempotency_key);

-- Callback
CREATE TABLE approval_callback(
  id             BIGSERIAL          PRIMARY KEY,
  approval_id    BIGINT             NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  target_service TEXT               NOT NULL,
  endpoint       TEXT               NOT NULL,
  signature_key  TEXT               NOT NULL,
  headers        JSONB              NOT NULL DEFAULT '{}'::jsonb,
  state          VARCHAR(32)        NOT NULL,
  retry_count    INT                NOT NULL DEFAULT 0,
  last_error     TEXT,
  next_retry_at  TIMESTAMPTZ
);
CREATE INDEX ix_cb_state_due        ON approval_callback(state, next_retry_at);

-- Idempotency
CREATE TABLE idempotency_keys(
  scope      VARCHAR(128)           NOT NULL,
  key        VARCHAR(255)           NOT NULL,
  created_at TIMESTAMPTZ            NOT NULL DEFAULT now(),
  PRIMARY KEY(scope, key)
);

-- Outbox
CREATE TABLE approval_outbox(
  id           BIGSERIAL            PRIMARY KEY,
  approval_id  BIGINT               NOT NULL,
  event_type   TEXT                 NOT NULL,
  payload      JSONB                NOT NULL,
  created_at   TIMESTAMPTZ          NOT NULL DEFAULT now(),
  published_at TIMESTAMPTZ
);
CREATE INDEX ix_outbox_unpublished  ON approval_outbox(id) WHERE published_at IS NULL;
