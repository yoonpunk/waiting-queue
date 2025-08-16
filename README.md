# 대기열 프로젝트
이 프로젝트는 대기열을 관리하는 시스템을 구현합니다.  
사용자는 대기열에 추가되거나 제거될 수 있으며, 대기열의 상태를 확인하고 그 다음 단계로 진행할 수 있습니다.  
Grafana K6를 활용하여 Spike 시나리오를 통해 대기열 등록 및 조회 성능을 테스트할 수 있습니다.

## 빠른 실행
아래 과정으로 프로젝트를 빠르게 실행하고, 간단한 테스트를 진행할 수 있습니다.

### 도커를 통한 Redis 구성
```bash
# redis 이미지 받기
docker image pull redis

# Redis 컨테이너를 실행
docker run -d --name wq-redis -p 6379:6379 redis redis-server --requirepass "wq-redis"
```

### Scheduler 서버와 API 서버 실행
- Scheduler 서버는 대기열 상태를 확인하고 다음 단계로 진행합니다.
- API 서버는 대기열 등록 및 조회를 위한 대고객 API를 제공합니다.
- 빠른실행을 위하여 Scheduler 서버를 API 서버보다 먼저 실행해야 합니다.
  - Scheduler 서버 기동 시, 대기열 대상 상품 정보를 초기화하는 로직이 있습니다. (로컬 최초 실행 시, 테스트 상품 Redis에 초기화 WaitingItemInitializer.java 참고)
  - 실제 환경에서는 대기열 상품 정보를 외부(Kafka)에서 전달받을 수 있으므로 기동 순서가 중요하지 않습니다. 
- API 서버는 기동 시 대기열 등록/조회 API에 대한 WarmUp을 수행합니다.

```bash
# Scheduler 서버 기동. 기본 포트는 8081입니다. (반드시 프로젝트 홈경로에서 실행해주세요)
# [WaitingItemInitializer] Initialized waiting items: ~~~ 로그가 출력되면 초기화가 완료된 것 입니다.
./gradlew :waiting-queue-scheduler:bootRun
```

```bash
# API 서버 기동. 기본 포트는 8080입니다. (반드시 프로젝트 홈경로에서 실행해주세요)
# ===== E2E Warm-up finished. Application is now fully ready. ===== 로그가 출력되면 WarmUp이 완료된 것입니다.
./gradlew :waiting-queue-api:bootRun
```

### 대기열 등록 및 조회 성능테스트 실행
간단한 수준의 Spike 시나리오 테스트를 진행합니다.

```bash
# k6 설치
brew install k6

# k6 설치 후, 아래 명령어로 Spike 시나리오 테스트를 실행합니다. (위 두 서버를 모두 실행시킨 후 반드시 프로젝트 홈경로에서 실행해주세요)
# 50명의 가상유저를 통해 초당 20건의 요청으로 12초 정도로 동작을 테스트하는 작은 수준의 테스트 시나리오를 수행합니다.
# 터미널에 테스트 진행사항과 결과가 출력되면 성공입니다.
k6 run k6scripts/spike-test.js
```
