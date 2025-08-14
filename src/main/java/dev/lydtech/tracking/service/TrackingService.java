package dev.lydtech.tracking.service;

import dev.lydtech.dispatch.event.DispatchCompleted;
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

    public static final String TRACKING_TOPIC = "tracking.status";

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void processDispatchPreparing(DispatchPreparing payload) throws Exception {
        log.info("Processing DispatchPreparing payload: {}", payload);

        // Process the DispatchPreparing information

        // 1. Create a tracking status update
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(payload.getOrderId())
                .status(Status.DISPATCH_PREPARING)
                .build();

        // 2. Send the tracking status to Kafka synchronously
        log.info("Sending tracking status PREPARING for order ID: {}", payload.getOrderId());
        kafkaTemplate.send(TRACKING_TOPIC, trackingStatusUpdated).get();

        log.info("Tracking Preparing processed : {}", trackingStatusUpdated);
    }

    public void processDispatchCompleted(DispatchCompleted payload) throws Exception {
        log.info("Processing DispatchCompleted payload: {}", payload);

        // Process the DispatchCompleted information

        // 1. Create a tracking status completed
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(payload.getOrderId())
                .status(Status.DISPATCH_COMPLETED)
                .build();

        // 2. Send the tracking status update to Kafka synchronously
        log.info("Sending tracking status COMPLETED for order ID: {}", payload.getOrderId());
        kafkaTemplate.send(TRACKING_TOPIC, trackingStatusUpdated).get();

        log.info("Tracking Completed processed : {}", trackingStatusUpdated);
    }
}
