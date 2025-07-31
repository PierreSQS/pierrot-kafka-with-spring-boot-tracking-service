package dev.lydtech.tracking.integration;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.tracking.config.TrackingConfiguration;
import dev.lydtech.tracking.service.TrackingService;
import dev.lydtech.tracking.util.TestEventData;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@SpringBootTest(classes = {TrackingConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(controlledShutdown = true)
class DispatchTrackingIntegrationTest {

    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";


    @Autowired
    KafkaTestListener kafkaTestListener;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;



    @BeforeEach
    void setUp() {

    }

    @Test
    void testDispatchTrackingFlow() throws Exception {

        // This test would verify the end-to-end flow of dispatch tracking
        // by sending a DispatchPreparing event and checking if it is received by the Kafka listener.

        log.info("Starting DispatchTrackingIntegrationTest...");
        DispatchPreparing dispatchPreparing = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());

        log.info("Sending DispatchPreparing event: {}", dispatchPreparing);
        sendEventMessage(DISPATCH_TRACKING_TOPIC, dispatchPreparing);

        log.info("Ending DispatchTrackingIntegrationTest...");

    }

    private void sendEventMessage(String topic, Object object) throws Exception {
        kafkaTemplate.send(MessageBuilder
                .withPayload(object)
                .setHeader(KafkaHeaders.TOPIC, topic)
                .build()).get();
    }

    @TestConfiguration
    static class TestConfig {

        @Bean
        public KafkaTestListener kafkaTestListener() {
            return new KafkaTestListener();
        }

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
