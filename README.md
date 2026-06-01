# 캠퍼스 탄소중립 시뮬레이터 — 백엔드 (인증 + AI 리포트 저장)

현재 구현 범위:
- 사용자 인증 (회원가입 / 로그인 / 내 정보) — 데모용 가벼운 JWT 인증
- AI 리포트 저장 / 조회 (프론트가 생성한 리포트 JSON을 받아 보관 — 갈래 2 방식)

아직 미구현 (다음 단계):
- 설비 마스터(equipment), 기존 설비 배치(preset_placement)
- 시나리오 저장/조회, 리더보드, 점수 계산
- ※ 설비 항목·계산 공식은 팀 회의 후 확정 예정

## 실행 방법
```
./gradlew bootRun
```
- 기본 포트: 8080
- DB: H2 인메모리 (재시작 시 초기화). 확인: http://localhost:8080/h2-console

## API 빠른 테스트
```bash
# 1. 회원가입
curl -X POST localhost:8080/auth/signup \
  -H "Content-Type: application/json" \
  -d '{"username":"greenplanner","password":"pw1234","displayName":"그린플래너"}'

# 2. 로그인 → 응답의 data.token 복사
curl -X POST localhost:8080/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"greenplanner","password":"pw1234"}'

# 3. 내 정보 (토큰 사용)
curl localhost:8080/auth/me \
  -H "Authorization: Bearer <위에서 받은 토큰>"

# 4. AI 리포트 저장 (시나리오 100번 가정)
curl -X POST localhost:8080/scenarios/100/report \
  -H "Content-Type: application/json" \
  -d '{
    "formatVersion":"inha-carbon-report.v1",
    "summary":"...",
    "strengths":["..."],
    "warnings":["..."],
    "recommendations":["..."],
    "notes":["..."],
    "createdAt":"2026-05-25T10:05:00Z",
    "model":"solar-pro3"
  }'

# 5. AI 리포트 조회
curl localhost:8080/scenarios/100/report
```

## 패키지 구조
```
domain      엔티티 + JSON 컨버터
repository  JPA 리포지토리
service     비즈니스 로직 + 예외
controller  REST 엔드포인트
dto         요청/응답 객체
security    JWT 발급/검증 필터
config      스프링 시큐리티 설정
common      공통 응답 포맷 + 전역 예외 핸들러
```

## 주의
- `jwt.secret`은 데모용 기본값입니다. 실제 배포 시 환경변수로 교체하세요.
- 리포트 저장은 시나리오당 1개(덮어쓰기) 구조입니다.
- 시나리오 엔티티가 추가되면 ReportService의 TODO(시나리오 존재 검증)를 연결해야 합니다.
