-- V10__20250928_create_approval_request_bear.sql (수정본: BEGIN/COMMIT 불필요)
CREATE SCHEMA IF NOT EXISTS bear;

-- 함수는 아예 bear 스키마로
CREATE OR REPLACE FUNCTION bear.set_updated_at() RETURNS trigger AS $$
BEGIN
  NEW.updated_at = now();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 이후엔 스키마 명시 + 함수도 bear로 호출
DROP TABLE IF EXISTS bear.approval_request CASCADE;

CREATE TABLE bear.approval_request (
  id BIGSERIAL PRIMARY KEY,
  title TEXT NOT NULL,
  content TEXT NOT NULL,
  requester_id TEXT NOT NULL,
  status TEXT NOT NULL,
  current_stage INT NOT NULL DEFAULT 1,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now()
);

CREATE INDEX IF NOT EXISTS ix_approval_request_status   ON bear.approval_request(status);
CREATE INDEX IF NOT EXISTS ix_approval_request_requester ON bear.approval_request(requester_id);

DROP TRIGGER IF EXISTS trig_approval_request_updated_at ON bear.approval_request;
CREATE TRIGGER trig_approval_request_updated_at
BEFORE UPDATE ON bear.approval_request
FOR EACH ROW EXECUTE FUNCTION bear.set_updated_at();

-- stage
DROP TABLE IF EXISTS bear.approval_stage CASCADE;
CREATE TABLE bear.approval_stage(
  id BIGSERIAL PRIMARY KEY,
  approval_request_id BIGINT NOT NULL REFERENCES bear.approval_request(id) ON DELETE CASCADE,
  order_no INT NOT NULL,
  status TEXT NOT NULL,
  required_count INT,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (approval_request_id, order_no)
);
DROP TRIGGER IF EXISTS trig_approval_stage_updated_at ON bear.approval_stage;
CREATE TRIGGER trig_approval_stage_updated_at
BEFORE UPDATE ON bear.approval_stage
FOR EACH ROW EXECUTE FUNCTION bear.set_updated_at();

-- assignment
DROP TABLE IF EXISTS bear.approval_assignment CASCADE;
CREATE TABLE bear.approval_assignment(
  id BIGSERIAL PRIMARY KEY,
  approval_stage_id BIGINT NOT NULL REFERENCES bear.approval_stage(id) ON DELETE CASCADE,
  approver_id TEXT NOT NULL,
  comment TEXT,
  decided_at timestamptz,
  created_at timestamptz NOT NULL DEFAULT now(),
  updated_at timestamptz NOT NULL DEFAULT now(),
  UNIQUE (approval_stage_id, approver_id)
);
CREATE INDEX IF NOT EXISTS ix_assignment_stage ON bear.approval_assignment(approval_stage_id);
DROP TRIGGER IF EXISTS trig_approval_assignment_updated_at ON bear.approval_assignment;
CREATE TRIGGER trig_approval_assignment_updated_at
BEFORE UPDATE ON bear.approval_assignment
FOR EACH ROW EXECUTE FUNCTION bear.set_updated_at();
