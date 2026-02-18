-- hobom_logs 검색 조건으로 사용되는 컬럼에 인덱스 추가
-- 필터: service_type, http_method, status_code, timestamp 범위
CREATE INDEX idx_hobom_logs_service_type ON hobom_logs (service_type);
CREATE INDEX idx_hobom_logs_status_code ON hobom_logs (status_code);
CREATE INDEX idx_hobom_logs_http_method ON hobom_logs (http_method);
CREATE INDEX idx_hobom_logs_timestamp ON hobom_logs (timestamp DESC);
