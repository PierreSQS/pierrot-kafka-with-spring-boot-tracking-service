package dev.lydtech.tracking.service;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.dispatch.event.Status;
import dev.lydtech.dispatch.event.TrackingStatusUpdated;
import dev.lydtech.tracking.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class TrackingServiceTest {

    private TrackingService trackingService;

    private KafkaTemplate<String, Object> kafkaProducerMock;

    @BeforeEach
    void setUp() {
        kafkaProducerMock = mock(KafkaTemplate.class);
        trackingService = new TrackingService(kafkaProducerMock);
    }

    @Test
    void process_Success() throws Exception {

        // Given a DispatchPreparing event
        DispatchPreparing dispatchPreparing = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());
        TrackingStatusUpdated trackingStatusUpdated = TrackingStatusUpdated.builder()
                .orderId(dispatchPreparing.getOrderId())
                .status(Status.DISPATCH_PREPARING)
                .build();

        // Mock the Kafka producer to simulate sending the event
        given(kafkaProducerMock.send(eq("tracking.status"), any(TrackingStatusUpdated.class)))
                .willReturn(mock(CompletableFuture.class));

        // When the process method is called
        trackingService.process(dispatchPreparing);

        // Verify that the event was sent to the Kafka topic
        verify(kafkaProducerMock).send("tracking.status", trackingStatusUpdated);
    }

    @Test
    void process_ProducerThrowsException() {
        // Given a DispatchPreparing event
        DispatchPreparing dispatchPreparing = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());

        // Mock the Kafka producer to throw an exception when sending the event
        given(kafkaProducerMock.send(eq("tracking.status"), any(TrackingStatusUpdated.class)))
                .willThrow(new RuntimeException("Producer failure"));

        // When the process method is called, it should throw an exception
        assertThatThrownBy(() -> trackingService.process(dispatchPreparing))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Producer failure");

    }
}