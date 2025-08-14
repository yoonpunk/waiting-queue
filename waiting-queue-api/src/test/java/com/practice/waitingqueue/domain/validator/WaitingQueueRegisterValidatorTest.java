package com.practice.waitingqueue.domain.validator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.practice.waitingqueue.domain.entity.WaitingItem;
import com.practice.waitingqueue.domain.exception.WaitingQueueRegisterFailedException;
import com.practice.waitingqueue.testdouble.FakeWaitingItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WaitingQueueRegisterValidatorTest {

    @Test
    void 대기열_상품_정보가_없는_경우_예외가_발생한다() {
        // given
        long nonExistentItemId = 999L;
        FakeWaitingItemRepository fakeWaitingItemRepository = new FakeWaitingItemRepository();
        WaitingQueueRegisterValidator sut = new WaitingQueueRegisterValidator(fakeWaitingItemRepository);

        // when & then
        final var exception = assertThrows(
            WaitingQueueRegisterFailedException.class,
            () -> sut.validateItemIdCanUseWaitingQueue(nonExistentItemId)
        );
        assertThat(exception.getErrorCode()).isEqualTo(WaitingQueueRegisterFailedException.ErrorCode.ITEM_CANNOT_USE_WAITING_QUEUE);
    }

    @Test
    void 대기열_상품이_더이상_대기열을_이용할_수_없으면_예외가_발생한다() {
        // given
        long itemId = 1L;
        int itemRemainCount = 0;
        WaitingItem waitingItem = WaitingItem.of(itemId, "testitem", itemRemainCount, true);

        FakeWaitingItemRepository fakeWaitingItemRepository = new FakeWaitingItemRepository();
        fakeWaitingItemRepository.save(waitingItem);

        WaitingQueueRegisterValidator sut = new WaitingQueueRegisterValidator(fakeWaitingItemRepository);

        // when & then
        final var exception = assertThrows(
            WaitingQueueRegisterFailedException.class,
            () -> sut.validateItemIdCanUseWaitingQueue(itemId)
        );

        assertThat(exception.getErrorCode()).isEqualTo(WaitingQueueRegisterFailedException.ErrorCode.ITEM_CANNOT_USE_WAITING_QUEUE);
    }

    @Test
    @DisplayName("대기열 상품이 대기열을 이용할 수 있으면 검증에 성공한다")
    void validateItemIdCanUseWaitingQueue_doesNotThrowException_whenItemCanBeUsed() {
        // given
        long itemId = 1L;
        int itemRemainCount = 10;
        WaitingItem waitingItem = WaitingItem.of(itemId, "testitem", itemRemainCount, true);

        FakeWaitingItemRepository fakeWaitingItemRepository = new FakeWaitingItemRepository();
        fakeWaitingItemRepository.save(waitingItem);

        WaitingQueueRegisterValidator sut = new WaitingQueueRegisterValidator(fakeWaitingItemRepository);

        // when & then
        assertDoesNotThrow(() -> sut.validateItemIdCanUseWaitingQueue(itemId));
    }
}
