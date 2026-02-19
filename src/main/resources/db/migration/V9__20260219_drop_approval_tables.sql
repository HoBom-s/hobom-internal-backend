-- ApprovalSystem 제거 (V7에서 생성된 테이블 삭제)
-- FK 의존 순서에 따라 역순으로 DROP
DROP TABLE IF EXISTS approval_assignment CASCADE;
DROP TABLE IF EXISTS approval_stage CASCADE;
DROP TABLE IF EXISTS approval_request CASCADE;
