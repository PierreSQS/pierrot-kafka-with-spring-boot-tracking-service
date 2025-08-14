package dev.lydtech.tracking.util;

import dev.lydtech.dispatch.event.DispatchCompleted;
import dev.lydtech.dispatch.event.DispatchPreparing;

import java.time.LocalDate;
import java.util.UUID;

public class TestEventData {

    public static DispatchPreparing buildDispatchPreparingEvent(UUID orderId) {
        return DispatchPreparing.builder()
                .orderId(orderId)
                .build();
    }
    public static DispatchCompleted buildDispatchCompletedEvent(UUID orderId) {
        return DispatchCompleted.builder()
                .orderId(orderId)
                .dateCompleted(LocalDate.now().toString())
                .build();
    }

}
