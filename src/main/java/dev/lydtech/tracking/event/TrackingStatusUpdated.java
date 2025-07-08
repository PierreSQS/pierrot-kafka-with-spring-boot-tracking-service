package dev.lydtech.tracking.event;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TrackingStatusUpdated {
    private UUID orderId;
    private Status status;
}
