import http from 'k6/http';
import { check, sleep } from 'k6';

// ===================================================================================
//                              [테스트 설정]
// ===================================================================================
// 이 섹션의 변수들을 수정하여 테스트 트래픽을 조절할 수 있습니다.

// 테스트 대상 서버 주소
const BASE_URL = 'http://localhost:8080';

// 테스트할 상품 ID 목록
const ITEM_IDS = ['11111', '22222', '33333', '44444', '55555', '66666', '77777', '88888'];

// ===================================================================================
//                              [k6 테스트 옵션]
// ===================================================================================
// Spike Test: 짧은 시간 동안 시스템에 과도한 부하를 주어 스트레스 상황을 시뮬레이션합니다.
// 적당한 수준으로 아래 값을 조정하여 테스트합니다. 현 프로젝트에서는 실제 테스트보다는 동작 확인을 목적으로 하므로 낮은수치로 설정합니다.

// 스파이크 부하 VUs (최대 가상 사용자 수)
const SPIKE_VUS = 50;
const SLEEP_TIME_SEC_PER_REQUEST = 3; // 한 VU에 대하여 각 요청 사이의 대기 시간 (초)

export const options = {
  stages: [
    // 1. 스파이크 부하 수준까지 급격하게 VUs 증가
    { duration: '1s', target: SPIKE_VUS },
    // 2. 스파이크 부하 유지
    { duration: '10s', target: SPIKE_VUS },
    // 3. 스파이크 종료 후 급격하게 VUs 감소
    { duration: '1s',  target: 0  }, // 램프다운 50 -> 0
  ],
  thresholds: {
    'http_req_failed': ['rate<0.01'], // http 에러율이 1% 미만이어야 함
    'http_req_duration': ['p(95)<100'], // 95%의 요청이 50ms 안에 처리되어야 함
  },
};

// ===================================================================================
//                              [VU별 상태 변수]
// ===================================================================================
// 각 VU는 독립적인 JS 런타임이므로, 이 변수들은 VU별로 고유한 상태를 유지합니다.
let waitingQueueToken = null;
let registeredItemId = null;

// ===================================================================================
//                              [테스트 실행 함수]
// ===================================================================================
export default function () {
  // 각 요청마다 고유한 사용자 ID를 생성하여 헤더에 추가
  const userId = Math.floor(Math.random() * 1000000) + 1;
  const params = {
    headers: {
      'user-id': `${userId}`,
      'Content-Type': 'application/json',
    },
  };

  // 해당 VU의 첫번째 요청인 경우 (등록된 토큰 없음, 상품 ID도 없음)
  if (waitingQueueToken === null || registeredItemId === null) {
    // --- 첫 번째 반복: 대기열 등록 ---
    const randomItemId = ITEM_IDS[Math.floor(Math.random() * ITEM_IDS.length)];
    registeredItemId = randomItemId; // 조회 단계에서 사용하기 위해 이 VU의 itemId를 저장

    const res = http.post(`${BASE_URL}/api/v1/item/${registeredItemId}/waiting-queue`, null, params);

    const checkPost = check(res, {
      'POST status is 200': (r) => r.status === 200,
      'response has data': (r) => r.json() && typeof r.json().data !== 'undefined',
    });

    // 요청이 성공하고 데이터가 있을 경우 토큰 추출
    if (checkPost) {
        const postRes = res.json();
        if (postRes.data && postRes.data.waitingQueueToken) {
            waitingQueueToken = postRes.data.waitingQueueToken;
        }
    }

  } else {
    // --- 두 번째 반복부터: 대기열 조회 ---
    if (waitingQueueToken && registeredItemId) {
      const res = http.get(`${BASE_URL}/api/v1/item/${registeredItemId}/waiting-queue/${waitingQueueToken}`, params);
      check(res, {
        'GET status is 200': (r) => r.status === 200,
      });
    }
  }

  sleep(SLEEP_TIME_SEC_PER_REQUEST); // 각 요청 사이에 대기 시간 추가
}
