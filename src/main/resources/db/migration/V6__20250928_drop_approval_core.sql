BEGIN;

DO $$
BEGIN
  RAISE NOTICE 'Dropping legacy approval objects if they exist...';
END $$;

DROP TABLE IF EXISTS approval_callback CASCADE;
DROP TABLE IF EXISTS approval_action CASCADE;
DROP TABLE IF EXISTS approval_step_approver CASCADE;
DROP TABLE IF EXISTS approval_step CASCADE;
DROP TABLE IF EXISTS approval_stage CASCADE;
DROP TABLE IF EXISTS approval_request CASCADE;
DROP TABLE IF EXISTS idempotency_keys CASCADE;
DROP TABLE IF EXISTS approval_outbox CASCADE;

DROP TRIGGER IF EXISTS trig_approval_request_updated_at   ON approval_request;
DROP TRIGGER IF EXISTS trig_approval_stage_updated_at     ON approval_stage;
DROP TRIGGER IF EXISTS trig_approval_assignment_updated_at ON approval_assignment; -- 새 스키마에 있을 예정
DROP FUNCTION IF EXISTS set_updated_at();

DO $inner$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'approval_status') THEN
    EXECUTE 'DROP TYPE approval_status';
  END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'stage_status') THEN
    EXECUTE 'DROP TYPE stage_status';
  END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'step_mode') THEN
    EXECUTE 'DROP TYPE step_mode';
  END IF;
  IF EXISTS (SELECT 1 FROM pg_type WHERE typname = 'step_status') THEN
    EXECUTE 'DROP TYPE step_status';
  END IF;
END
$inner$;

COMMIT;
