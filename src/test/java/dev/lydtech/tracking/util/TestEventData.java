package dev.lydtech.tracking.util;

import dev.lydtech.dispatch.event.DispatchCompleted;
import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.dispatch.event.Status;
import dev.lydtech.dispatch.event.TrackingStatusUpdated;

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

    public static TrackingStatusUpdated buildTrackingStatusUpdatedEvent(UUID orderId, Status status) {
        return TrackingStatusUpdated.builder()
                .orderId(orderId)
                .status(status)
                .build();
    }
}
