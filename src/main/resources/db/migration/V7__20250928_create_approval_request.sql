BEGIN;

CREATE OR REPLACE FUNCTION set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- =============== approval_request ===============
DROP TABLE IF EXISTS approval_request CASCADE;

CREATE TABLE approval_request (
  id               BIGSERIAL PRIMARY KEY,
  title            TEXT        NOT NULL,
  content          TEXT        NOT NULL,
  requester_id     TEXT        NOT NULL,
  status           TEXT        NOT NULL,
  current_stage    INT         NOT NULL DEFAULT 1,
  created_at       TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at       TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

CREATE INDEX ix_approval_request_status
  ON approval_request (status);
CREATE INDEX ix_approval_request_requester
  ON approval_request (requester_id);

CREATE TRIGGER trig_approval_request_updated_at
BEFORE UPDATE ON approval_request
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =============== approval_stage ===============
DROP TABLE IF EXISTS approval_stage CASCADE;

CREATE TABLE approval_stage (
  id                   BIGSERIAL PRIMARY KEY,
  approval_request_id  BIGINT      NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  order_no             INT         NOT NULL,
  status               TEXT        NOT NULL,
  required_count       INT         NULL,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (approval_request_id, order_no)
);

CREATE TRIGGER trig_approval_stage_updated_at
BEFORE UPDATE ON approval_stage
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

-- =============== approval_assignment ===============
DROP TABLE IF EXISTS approval_assignment CASCADE;

CREATE TABLE approval_assignment (
  id                   BIGSERIAL PRIMARY KEY,
  approval_stage_id    BIGINT      NOT NULL REFERENCES approval_stage(id) ON DELETE CASCADE,
  approver_id          TEXT        NOT NULL,
  comment              TEXT,
  decided_at           TIMESTAMPTZ,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  UNIQUE (approval_stage_id, approver_id)
);

CREATE INDEX ix_assignment_stage
  ON approval_assignment (approval_stage_id);

CREATE TRIGGER trig_approval_assignment_updated_at
BEFORE UPDATE ON approval_assignment
FOR EACH ROW EXECUTE FUNCTION set_updated_at();

COMMIT;
