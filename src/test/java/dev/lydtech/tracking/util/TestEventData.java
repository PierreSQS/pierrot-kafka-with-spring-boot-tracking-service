package dev.lydtech.tracking.util;

import dev.lydtech.tracking.event.DispatchPreparing;
import dev.lydtech.tracking.event.Status;
import dev.lydtech.tracking.event.TrackingStatusUpdated;

import java.util.UUID;

public class TestEventData {

    public static DispatchPreparing buildDispatchPreparingEvent(UUID orderId) {
        return DispatchPreparing.builder()
                .orderId(orderId)
                .build();
    }

    public static TrackingStatusUpdated buildTrackingStatusUpdatedEvent(UUID orderId, Status status) {
        return TrackingStatusUpdated.builder()
                .orderId(orderId)
                .status(status)
                .build();
    }
}
