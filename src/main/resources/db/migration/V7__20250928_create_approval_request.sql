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

-- =============== approval_stage ===============
DROP TABLE IF EXISTS approval_stage CASCADE;

CREATE TABLE approval_stage (
  id                   BIGSERIAL PRIMARY KEY,
  approval_request_id  BIGINT      NOT NULL REFERENCES approval_request(id) ON DELETE CASCADE,
  order_no             INT         NOT NULL,
  status               TEXT        NOT NULL,
  required_count       INT         NULL,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

-- =============== approval_assignment ===============
DROP TABLE IF EXISTS approval_assignment CASCADE;

CREATE TABLE approval_assignment (
  id                   BIGSERIAL PRIMARY KEY,
  approval_stage_id    BIGINT      NOT NULL REFERENCES approval_stage(id) ON DELETE CASCADE,
  approver_id          TEXT        NOT NULL,
  comment              TEXT,
  decided_at           TIMESTAMPTZ,
  created_at           TIMESTAMPTZ NOT NULL DEFAULT NOW(),
  updated_at           TIMESTAMPTZ NOT NULL DEFAULT NOW()
);

COMMIT;
