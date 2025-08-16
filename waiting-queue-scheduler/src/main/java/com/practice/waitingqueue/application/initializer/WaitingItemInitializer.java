package com.practice.waitingqueue.application.initializer;

import com.opencsv.bean.CsvToBeanBuilder;
import com.practice.waitingqueue.application.initializer.dto.WaitingItemInitializeCsvRow;
import com.practice.waitingqueue.domain.dto.WaitingItemSyncCommand;
import com.practice.waitingqueue.domain.service.WaitingItemSyncService;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.ResourceUtils;

@Slf4j
@Component
@RequiredArgsConstructor
public class WaitingItemInitializer {

    private static final String INITIALIZE_DATA_PATH = "classpath:waiting_item_initialize.csv";

    @Value("${waiting-queue.waiting-item.need-to-initialize}")
    private Boolean needToInitialize;

    private final WaitingItemSyncService waitingItemSyncService;
    @EventListener(ApplicationReadyEvent.class)
    public void initialize() {
        if (!needToInitialize) {
            log.info("[WaitingItemInitializer] Waiting item initialization is not needed.");
            return;
        }

        List<Long> syncedItemIdList = initializeWaitingItem();
        log.info("[WaitingItemInitializer] Initialized waiting items: {}", syncedItemIdList);
   }

    public List<Long> initializeWaitingItem() {
        List<WaitingItemSyncCommand> commandList = readWaitingItemsFromCsv();
        commandList.forEach(waitingItemSyncService::sync);

        return commandList.stream().map(WaitingItemSyncCommand::getItemId).toList();
    }

    private List<WaitingItemSyncCommand> readWaitingItemsFromCsv() {
        try (FileReader fileReader = new FileReader(ResourceUtils.getFile(INITIALIZE_DATA_PATH), StandardCharsets.UTF_8)) {
            final var rows = new CsvToBeanBuilder<WaitingItemInitializeCsvRow>(fileReader)
                .withType(WaitingItemInitializeCsvRow.class)
                .build()
                .parse();

            return rows.stream()
                .map(WaitingItemInitializeCsvRow::toCommand)
                .toList();
        } catch (Exception e) {
            log.error("[WaitingItemInitializer] Failed to read waiting_item_initialize.csv", e);
            return Collections.emptyList();
        }
    }
}
