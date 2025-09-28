-- V8__drop_legacy_approval_in_public_and_bear.sql

-- 0) 알림
DO $$
BEGIN
  RAISE NOTICE 'Dropping legacy approval objects in schemas: public, bear (if they exist)...';
END $$;

-- 1) 트리거/함수 먼저 (양쪽 스키마에 대해 시도)
DO $$
BEGIN
  IF EXISTS (SELECT 1 FROM pg_trigger t JOIN pg_class c ON t.tgrelid = c.oid
             JOIN pg_namespace n ON c.relnamespace = n.oid
             WHERE t.tgname = 'trig_approval_request_updated_at' AND n.nspname IN ('public','bear')) THEN
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_request_updated_at ON public.approval_request';
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_request_updated_at ON bear.approval_request';
  END IF;

  IF EXISTS (SELECT 1 FROM pg_trigger t JOIN pg_class c ON t.tgrelid = c.oid
             JOIN pg_namespace n ON c.relnamespace = n.oid
             WHERE t.tgname = 'trig_approval_stage_updated_at' AND n.nspname IN ('public','bear')) THEN
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_stage_updated_at ON public.approval_stage';
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_stage_updated_at ON bear.approval_stage';
  END IF;

  IF EXISTS (SELECT 1 FROM pg_trigger t JOIN pg_class c ON t.tgrelid = c.oid
             JOIN pg_namespace n ON c.relnamespace = n.oid
             WHERE t.tgname = 'trig_approval_assignment_updated_at' AND n.nspname IN ('public','bear')) THEN
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_assignment_updated_at ON public.approval_assignment';
    EXECUTE 'DROP TRIGGER IF EXISTS trig_approval_assignment_updated_at ON bear.approval_assignment';
  END IF;
END $$;

-- 2) 테이블 드랍(스키마 명시)
DROP TABLE IF EXISTS public.approval_callback        CASCADE;
DROP TABLE IF EXISTS bear.approval_callback          CASCADE;

DROP TABLE IF EXISTS public.approval_action          CASCADE;
DROP TABLE IF EXISTS bear.approval_action            CASCADE;

DROP TABLE IF EXISTS public.approval_step_approver   CASCADE;
DROP TABLE IF EXISTS bear.approval_step_approver     CASCADE;

DROP TABLE IF EXISTS public.approval_step            CASCADE;
DROP TABLE IF EXISTS bear.approval_step              CASCADE;

DROP TABLE IF EXISTS public.approval_stage           CASCADE;
DROP TABLE IF EXISTS bear.approval_stage             CASCADE;

DROP TABLE IF EXISTS public.approval_request         CASCADE;
DROP TABLE IF EXISTS bear.approval_request           CASCADE;

DROP TABLE IF EXISTS public.idempotency_keys         CASCADE;
DROP TABLE IF EXISTS bear.idempotency_keys           CASCADE;

DROP TABLE IF EXISTS public.approval_outbox          CASCADE;
DROP TABLE IF EXISTS bear.approval_outbox            CASCADE;

-- 3) 공통 함수 제거
DROP FUNCTION IF EXISTS set_updated_at();

-- 4) enum 타입 제거 (타입은 스키마 소속이 아님)
DROP TYPE IF EXISTS bear.approval_status CASCADE;
DROP TYPE IF EXISTS public.approval_status CASCADE;

DROP TYPE IF EXISTS bear.stage_status CASCADE;
DROP TYPE IF EXISTS public.stage_status CASCADE;

DROP TYPE IF EXISTS bear.step_mode CASCADE;
DROP TYPE IF EXISTS public.step_mode CASCADE;

DROP TYPE IF EXISTS bear.step_status CASCADE;
DROP TYPE IF EXISTS public.step_status CASCADE;