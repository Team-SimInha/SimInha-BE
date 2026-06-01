# 캠퍼스 탄소중립 시뮬레이터 — 백엔드

## 구현 범위 (현재)

- **인증 시스템** — 회원가입/로그인/내 정보 (JWT, BCrypt)
- **AI 리포트 저장/조회** — 프론트가 생성한 리포트 JSON 보관 (갈래 2)
- **설비 마스터** — `GET /equipments` (items.js 시드 자동 주입)
- **기존 설비** — `GET /preset-placements`, `GET /preset-placements/baseline`
- **시나리오** — 저장/수정/조회/목록/삭제 (metrics는 JSON 통째 저장: A 방식)
- **리더보드** — 제출/조회 (시나리오 스냅샷 기반 + 직접 제출 모두 지원)

## 실행
```
./gradlew bootRun
```
- 포트: 8080
- DB: H2 인메모리 (재시작 시 초기화). 확인: http://localhost:8080/h2-console
- 시작 시 `DataInitializer`가 설비·기존 설비 시드 데이터 자동 주입

## 주요 엔드포인트

| 메서드 | 경로 | 설명 |
|---|---|---|
| POST | `/auth/signup` | 회원가입 |
| POST | `/auth/login` | 로그인 (JWT 발급) |
| GET  | `/auth/me` | 내 정보 (토큰 필요) |
| GET  | `/equipments` | 설비 마스터 목록 |
| GET  | `/equipments/{id}` | 설비 상세 |
| GET  | `/preset-placements` | 기존 설비 배치 목록 |
| GET  | `/preset-placements/baseline` | 베이스라인 집계 |
| POST | `/scenarios` | 시나리오 저장 |
| PUT  | `/scenarios/{id}` | 시나리오 수정 |
| GET  | `/scenarios/{id}` | 시나리오 조회 |
| GET  | `/scenarios?nickname=` | 시나리오 목록 |
| DELETE | `/scenarios/{id}` | 시나리오 삭제 |
| POST | `/scenarios/{id}/report` | AI 리포트 저장 (덮어쓰기) |
| GET  | `/scenarios/{id}/report` | AI 리포트 조회 |
| POST | `/leaderboard` | 점수 제출 |
| GET  | `/leaderboard?sortBy=&page=&size=` | 랭킹 조회 |

## 빠른 테스트
```bash
# 설비 목록
curl localhost:8080/equipments

# 베이스라인
curl localhost:8080/preset-placements/baseline

# 시나리오 저장 (metrics는 자유 JSON)
curl -X POST localhost:8080/scenarios \
  -H "Content-Type: application/json" \
  -d '{
    "name":"테스트 시나리오",
    "nickname":"그린플래너",
    "items":[
      {"id":"c1","type":"solar_self","lng":126.654,"lat":37.451,"qty":3,
       "effectiveCoeff":506,"locationName":"5호관 옥상","zoneType":"rooftop"}
    ],
    "metrics":{
      "user":{"netSaving":1584,"energyKwh":3960,"totalCost":9000000,"efficiencyScore":176},
      "baseline":{"netSaving":5760,"energyKwh":14400},
      "total":{"netSaving":7344,"energyKwh":18360}
    }
  }'

# 리더보드 제출 (시나리오 기반)
curl -X POST localhost:8080/leaderboard \
  -H "Content-Type: application/json" \
  -d '{"scenarioId":1,"nickname":"그린플래너"}'

# 리더보드 조회 (효율 점수 정렬)
curl "localhost:8080/leaderboard?sortBy=efficiency_score&page=0&size=10"
```

## 패키지 구조
```
domain       엔티티 (User, Equipment, PresetPlacement, Scenario, ScenarioPlacedItem,
             LeaderboardEntry, AiReport, StringListConverter)
repository   JPA 리포지토리
service      비즈니스 로직 + 예외
controller   REST 엔드포인트
dto          요청/응답 객체
security     JWT 발급/검증 필터
config       SecurityConfig, DataInitializer
common       공통 응답 포맷, 전역 예외 핸들러
```

## 설계 메모

- **metrics 저장 방식**: JSON 컬럼 통째 저장 (A 방식). 프론트 metrics 구조 변경 시 BE 스키마 변경 불필요.
- **시나리오 수정**: 배치 아이템은 전체 교체 (delete + insert). 단순하고 일관성 보장.
- **리더보드 신뢰성**: scenarioId가 있으면 서버의 시나리오 스냅샷에서 점수를 읽어옴. 클라이언트가 보낸 점수를 신뢰하지 않음.
- **사용자 소유권 확장 자리**: `scenario.user_id`, `leaderboard.user_id`가 nullable로 미리 마련됨. 추후 인증 사용자와 연결 가능.

## 다음 확장 후보
- `/scenarios/{id}/report` 저장 시 시나리오 존재 검증 (현재 TODO 주석)
- 시나리오와 리더보드의 `user_id` 채우기 (B 방식 전환)
- 점수 계산 검증 (서버 측 재계산 정책)
