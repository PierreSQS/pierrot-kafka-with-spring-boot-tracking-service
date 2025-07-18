package dev.lydtech.tracking.handler;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.tracking.service.TrackingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class DispatchTrackingHandler {

    private final TrackingService trackingService;


    /**
     * This method listens to the "dispatch.tracking" topic and processes incoming messages.
     * It is annotated with @KafkaListener to indicate that it is a Kafka consumer.
     *
     * @param payload The message payload received from the Kafka topic.
     */
    @KafkaListener(
            id = "trackingConsumerClient",
            topics = "dispatch.tracking",
            groupId = "tracking.dispatch.tracking.consumer",
            containerFactory = "kafkaListenerContainerFactory"
    )
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
}
