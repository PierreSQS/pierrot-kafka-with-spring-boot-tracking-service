package dev.lydtech.tracking.handler;

import dev.lydtech.dispatch.event.DispatchCompleted;
import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
@KafkaListener(
        id = "trackingConsumerClient",
        topics = "dispatch.tracking",
        groupId = "tracking.dispatch.tracking.consumer",
        containerFactory = "kafkaListenerContainerFactory"
)
public class DispatchTrackingHandler {

    private final TrackingService trackingService;


    /**
     * Handles DispatchPreparing events received from the "dispatch.tracking" Kafka topic.
     * Invoked automatically when a DispatchPreparing message is consumed.
     *
     * @param payload the DispatchPreparing event payload received from Kafka
     */
    @KafkaHandler
    public void listen(DispatchPreparing payload) {
        // This method will be called when a message is received from the "dispatch.tracking" topic
        // The payload is expected to be a DispatchPreparing object
        log.info("Received DispatchPreparing payload: {}", payload);

        try {
            trackingService.process(payload);
        } catch (Exception e) {
            log.error("Error processing tracking message: {}", payload, e);
        }
    }

    /**
     * Handles messages of type DispatchCompleted received from the "dispatch.tracking" Kafka topic.
     * Processes the payload using the trackingService.
     *
     * @param payload the DispatchCompleted event payload received from Kafka
     */
    @KafkaHandler
    public void listen(DispatchCompleted payload) {
        // This method will be called when a message is received from the "dispatch.tracking" topic
        // The payload is expected to be a DispatchPreparing object
        log.info("Received DispatchCompleted payload: {}", payload);

        try {
            trackingService.process(payload);
        } catch (Exception e) {
            log.error("Error processing tracking message: {}", payload, e);
        }
    }


}
