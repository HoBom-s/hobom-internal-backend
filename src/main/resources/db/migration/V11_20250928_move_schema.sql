-- V11__move_everything_public_to_bear.sql
-- 목적: public 스키마에 남아있는 사용자 오브젝트를 bear 스키마로 이동
-- 주의: 시스템 오브젝트 및 flyway_schema_history 는 제외

CREATE SCHEMA IF NOT EXISTS bear;

-- 0) 옵션: 중복 테이블 처리 정책
--    true  = bear에 동일 테이블이 이미 있으면, 구조가 동일하고 bear가 비어 있으면 public 데이터를 bear로 이관 후 public 테이블 삭제
--    false = 중복 테이블은 건너뜀(수동 정리 필요)
DO $$
BEGIN
  IF current_setting('app.move_public_to_bear.drop_public_if_merged', true) IS NULL THEN
    PERFORM set_config('app.move_public_to_bear.drop_public_if_merged', 'true', true);
  END IF;
END $$;

-- 1) 함수 보장 (updated_at 트리거용)
DO $$
BEGIN
  IF NOT EXISTS (
    SELECT 1 FROM pg_proc p
    JOIN pg_namespace n ON p.pronamespace = n.oid
    WHERE n.nspname = 'bear' AND p.proname = 'set_updated_at'
  ) THEN
    EXECUTE $f$
      CREATE OR REPLACE FUNCTION bear.set_updated_at() RETURNS trigger AS $$
      BEGIN
        NEW.updated_at = now();
        RETURN NEW;
      END;
      $$ LANGUAGE plpgsql;
    $f$;
  END IF;
END $$;

-- 2) 테이블 이동 (flyway_schema_history 제외)
DO $$
DECLARE
  t         text;
  drop_dup  boolean := (current_setting('app.move_public_to_bear.drop_public_if_merged', true) = 'true');
  col_list  text;
  bear_empty boolean;
  same_cols boolean;
BEGIN
  FOR t IN
    SELECT table_name
    FROM information_schema.tables
    WHERE table_schema = 'public'
      AND table_type = 'BASE TABLE'
      AND table_name <> 'flyway_schema_history'
    ORDER BY table_name
  LOOP
    IF NOT EXISTS (
      SELECT 1 FROM information_schema.tables
      WHERE table_schema='bear' AND table_name=t
    ) THEN
      -- (a) bear에 동일 테이블이 없으면: public -> bear 스키마 이동
      -- 트리거/인덱스/FK는 ALTER TABLE SET SCHEMA 시 함께 이동됨
      EXECUTE format('ALTER TABLE public.%I SET SCHEMA bear;', t);

      -- (a-1) 해당 테이블의 OWNED SEQUENCE(IDENTITY/serial)도 함께 이동 + 기본값 nextval 경로 수정
      PERFORM 1 FROM pg_class c
      WHERE c.relkind='S' AND c.relname=t||'_id_seq' AND
            EXISTS (SELECT 1 FROM pg_namespace n WHERE n.oid=c.relnamespace AND n.nspname='public');

      -- 소유된 시퀀스 찾아 이동 (pg_depend로 탐색)
      PERFORM (
        WITH seqs AS (
          SELECT s.oid AS seq_oid, s.relname AS seq_name,
                 a.attname AS col_name
          FROM pg_class s
          JOIN pg_namespace sn ON sn.oid = s.relnamespace
          JOIN pg_depend d ON d.objid = s.oid AND d.deptype = 'a'
          JOIN pg_class tbl ON tbl.oid = d.refobjid
          JOIN pg_namespace tn ON tn.oid = tbl.relnamespace
          JOIN pg_attribute a ON a.attrelid = tbl.oid AND a.attnum = d.refobjsubid
          WHERE sn.nspname='public' AND tn.nspname='bear' AND tbl.relname = t AND s.relkind='S'
        )
        SELECT
          (SELECT pg_catalog.pg_advisory_lock(hashtext('move_seq:'||t))) -- 잠금(동시 실행 방지)
      );

      FOR col_list IN
        SELECT format('%I', col_name)
        FROM (
          SELECT a.attname AS col_name
          FROM pg_class tbl
          JOIN pg_namespace tn ON tn.oid = tbl.relnamespace
          JOIN pg_attribute a ON a.attrelid = tbl.oid AND a.attnum > 0 AND NOT a.attisdropped
          WHERE tn.nspname='bear' AND tbl.relname = t
          ORDER BY a.attnum
        ) q
      LOOP
        -- no-op: 위 쿼리는 컬럼 순서 확인용, 아래 시퀀스 처리에서 사용
        NULL;
      END LOOP;

      -- 소유 시퀀스 실제 이동 + 기본값 수정
      PERFORM (
        WITH seqs AS (
          SELECT sn.nspname AS seq_schema, s.relname AS seq_name, a.attname AS col_name
          FROM pg_class s
          JOIN pg_namespace sn ON sn.oid = s.relnamespace
          JOIN pg_depend d ON d.objid = s.oid AND d.deptype = 'a'
          JOIN pg_class tbl ON tbl.oid = d.refobjid
          JOIN pg_namespace tn ON tn.oid = tbl.relnamespace
          JOIN pg_attribute a ON a.attrelid = tbl.oid AND a.attnum = d.refobjsubid
          WHERE sn.nspname='public' AND tn.nspname='bear' AND tbl.relname = t AND s.relkind='S'
        )
        SELECT
          CASE
            WHEN (SELECT COUNT(*) FROM seqs) > 0 THEN (
              SELECT string_agg(cmd, E'\n')
              FROM (
                SELECT format('ALTER SEQUENCE %I.%I SET SCHEMA bear;', seq_schema, seq_name) AS cmd
                UNION ALL
                SELECT format('ALTER TABLE bear.%I ALTER COLUMN %I SET DEFAULT nextval(%L);',
                              t, col_name, 'bear.'||seq_name)
                FROM seqs
              ) x
            ) ELSE NULL
          END
      ) INTO col_list;

      IF col_list IS NOT NULL THEN
        EXECUTE col_list;
      END IF;

    ELSE
      -- (b) public과 bear 둘 다 있으면
      IF drop_dup THEN
        -- 구조 동일 여부
        SELECT NOT EXISTS (
          SELECT 1
          FROM (
            SELECT column_name, data_type, is_nullable, ordinal_position
            FROM information_schema.columns
            WHERE table_schema='public' AND table_name=t
          ) p
          FULL OUTER JOIN (
            SELECT column_name, data_type, is_nullable, ordinal_position
            FROM information_schema.columns
            WHERE table_schema='bear' AND table_name=t
          ) b
          USING (column_name, data_type, is_nullable, ordinal_position)
          WHERE (p.column_name IS NULL OR b.column_name IS NULL)
        ) INTO same_cols;

        -- bear 테이블이 비어있는지
        EXECUTE format('SELECT NOT EXISTS (SELECT 1 FROM bear.%I)', t) INTO bear_empty;

        IF same_cols AND bear_empty THEN
          -- 데이터 이관 후 public 테이블 제거
          EXECUTE format(
            'INSERT INTO bear.%I SELECT * FROM public.%I;', t, t
          );
          EXECUTE format('DROP TABLE public.%I CASCADE;', t);
        ELSE
          RAISE NOTICE 'Skip dropping public.%, reason: structure differs or bear not empty', t;
        END IF;
      ELSE
        RAISE NOTICE 'Table % exists in both public and bear -> skipped (set drop flag to true to merge/drop)', t;
      END IF;
    END IF;
  END LOOP;
END $$;

-- 3) 뷰/머티리얼라이즈드 뷰 이동
DO $$
DECLARE v text;
BEGIN
  -- 일반 뷰
  FOR v IN
    SELECT table_name
    FROM information_schema.views
    WHERE table_schema='public'
      AND table_name <> 'flyway_schema_history'
  LOOP
    IF NOT EXISTS (
      SELECT 1 FROM information_schema.views
      WHERE table_schema='bear' AND table_name=v
    ) THEN
      EXECUTE format('ALTER VIEW public.%I SET SCHEMA bear;', v);
    ELSE
      RAISE NOTICE 'View % exists in both schemas, skipping', v;
    END IF;
  END LOOP;

  FOR v IN
    SELECT c.relname
    FROM pg_class c
    JOIN pg_namespace n ON n.oid = c.relnamespace
    WHERE n.nspname='public' AND c.relkind='m'
  LOOP
    IF NOT EXISTS (
      SELECT 1
      FROM pg_class c
      JOIN pg_namespace n ON n.oid = c.relnamespace
      WHERE n.nspname='bear' AND c.relname=v AND c.relkind='m'
    ) THEN
      EXECUTE format('ALTER MATERIALIZED VIEW public.%I SET SCHEMA bear;', v);
    ELSE
      RAISE NOTICE 'Materialized view % exists in both schemas, skipping', v;
    END IF;
  END LOOP;
END $$;
