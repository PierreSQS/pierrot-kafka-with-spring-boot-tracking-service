package dev.lydtech.tracking.service;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.dispatch.event.Status;
import dev.lydtech.dispatch.event.TrackingStatusUpdated;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class TrackingService {

    private static final String TRACKING_TOPIC = "tracking.status";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void process(DispatchPreparing payload) throws Exception {
        log.info("Processing DispatchPreparing payload: {}", payload);

        // Process the DispatchPreparing information

        // 1. Create a tracking status update
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(payload.getOrderId())
                .status(Status.DISPATCH_PREPARING)
                .build();

        // 2. Send the tracking status update to Kafka synchronously
        log.info("Sending tracking status update for order ID: {}", payload.getOrderId());
        kafkaTemplate.send(TRACKING_TOPIC, trackingStatusUpdated).get();

        log.info("Tracking information processed : {}", trackingStatusUpdated);
    }
}
