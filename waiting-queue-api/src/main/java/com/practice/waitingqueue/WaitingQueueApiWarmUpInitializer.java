package com.practice.waitingqueue;

import com.practice.waitingqueue.interfaces.dto.WaitingQueueRegisterResponse;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingQueueApiWarmUpInitializer {

    private static final int WARM_UP_TOKEN_COUNT = 20;
    private static final long NOT_EXISTS_WARM_UP_USER_ID = -1000L;

    private final RestTemplateBuilder restTemplateBuilder;
    private final WarmUpProperties warmUpProperties;

    @Value("${spring.application.name:null}")
    private String applicationName;

    /**
     * 애플리케이션의 End-to-End 웜업을 수행합니다.
     *
     * 이 메소드는 WebServerInitializedEvent를 리스닝하여, 내장 웹 서버(Tomcat 등)가 초기화되고
     * HTTP 요청을 받을 준비가 된 직후, 그리고 ApplicationReadyEvent가 발행되어 애플리케이션이
     * 완전히 준비 상태가 되기 전에 호출됩니다.
     *
     * 스프링의 이벤트 리스너는 기본적으로 동기(Synchronous) 방식으로 동작하므로, 이 메소드의 실행이
     * 완료될 때까지 애플리케이션의 시작 절차가 블로킹됩니다. 이를 통해 웜업이 끝나기 전에
     * 외부의 실제 트래픽이 들어오는 것을 방지하고, 첫 요청부터 최적의 성능을 보장할 수 있도록 합니다.
     * (단, 이는 Readiness Probe를 사용하는 프로덕션 환경 기준입니다.)
     *
     * 웜업 방식은 RestTemplate을 사용하여 자기 자신의 API를 직접 호출함으로써, 등록/조회 흐름을 모두 테스트하여
     * 웹 계층부터 서비스, 인프라 계층까지 모든 스택을 관통하는 실제 요청을 시뮬레이션합니다.
     *
     * @param event WebServerInitializedEvent 이벤트 객체. 웹 서버 포트 정보를 얻는 데 사용됩니다.
     */
    @EventListener(WebServerInitializedEvent.class)
    public void warmUp(WebServerInitializedEvent event) {
        if (StringUtils.isBlank(applicationName) || !applicationName.equals("waiting-queue-api")) {
            log.info("===== E2E Warm-up is skipped. Application name is not 'waiting-queue-api'. =====");
            return;
        }

        if (warmUpProperties.isWarmUpDisabled()) {
            log.info("===== E2E Warm-up is disabled. ======");
            return;
        }

        log.info("===== E2E Warm-up started (after server is up, before app is ready). =====");

        int port = event.getWebServer().getPort();
        RestTemplate restTemplate = restTemplateBuilder.rootUri("http://localhost:" + port).build();

        for (Long itemId : warmUpProperties.itemIds) {
            try {
                log.info("Warming up for itemId: {}", itemId);
                for (int i = 0; i < WARM_UP_TOKEN_COUNT; i++) {
                    final var headers = new HttpHeaders();
                    headers.set("user-id", String.valueOf(NOT_EXISTS_WARM_UP_USER_ID));
                    final var requestEntity = new HttpEntity<>(headers);

                    final var registerResponse = restTemplate.exchange(
                        "/api/v1/item/{itemId}/waiting-queue",
                        HttpMethod.POST,
                        requestEntity,
                        WaitingQueueRegisterResponse.class,
                        itemId
                    ).getBody();

                    if (registerResponse == null || registerResponse.getWaitingQueueToken() == null) {
                        log.warn("Warm-up registration for itemId {} returned null or no token.", itemId);
                        continue;
                    }

                    final var token = registerResponse.getWaitingQueueToken();

                    restTemplate.exchange(
                        "/api/v1/item/{itemId}/waiting-queue/{waitingQueueToken}",
                        HttpMethod.GET,
                        requestEntity,
                        Void.class,
                        itemId, token
                    );
                }
                log.info("Successfully warmed up for itemId: {}", itemId);
            } catch (Exception e) {
                log.error("Error during warm-up for itemId: {}", itemId, e);
            }
        }

        log.info("===== E2E Warm-up finished. Application is now fully ready. =====");
    }

    @ConfigurationProperties(prefix = "waiting-queue.warm-up")
    @Component
    @Getter @Setter
    public static class WarmUpProperties {
        private boolean enabled = false;        // 기본값 false
        private List<Long> itemIds = List.of(); // 기본값 빈 리스트
        private int requestCountPerItem = 20;   // 기본값 20

        public boolean isWarmUpDisabled() {
            return !enabled || itemIds.isEmpty();
        }
    }
}
