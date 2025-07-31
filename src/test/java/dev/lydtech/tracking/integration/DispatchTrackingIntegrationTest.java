package dev.lydtech.tracking.integration;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.tracking.config.TrackingConfiguration;
import dev.lydtech.tracking.service.TrackingService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest(classes = {TrackingConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(controlledShutdown = true)
class DispatchTrackingIntegrationTest {



    @BeforeEach
    void setUp() {

    }

    @Test
    void testDispatchTrackingFlow() {
    }

    // Kafka Listener Container
    public static class KafkaTestListener {

        AtomicInteger dispatchedPreparingCounter = new AtomicInteger(0);

        @KafkaListener(topics = TrackingService.TRACKING_TOPIC, groupId = "KafkaIntegrationTestGroup",
                containerFactory = "kafkaListenerContainerFactory")

        public void listen(final @Payload DispatchPreparing payload) {
            log.info("Received DispatchPreparing payload: {}", payload);
            dispatchedPreparingCounter.incrementAndGet();

        }

    }
}
