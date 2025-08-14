package dev.lydtech.tracking.integration;

import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.dispatch.event.TrackingStatusUpdated;
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
import org.springframework.kafka.config.KafkaListenerEndpointRegistry;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.kafka.test.EmbeddedKafkaBroker;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.kafka.test.utils.ContainerTestUtils;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.awaitility.Awaitility.await;
import static org.hamcrest.Matchers.equalTo;

@Slf4j
@SpringBootTest(classes = {TrackingConfiguration.class})
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@EmbeddedKafka(kraft = true, controlledShutdown = true)
class DispatchTrackingIntegrationTest {

    private static final String DISPATCH_TRACKING_TOPIC = "dispatch.tracking";


    @Autowired
    KafkaTestListener kafkaTestListener;

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    EmbeddedKafkaBroker embeddedKafkaBroker;

    @Autowired
    KafkaListenerEndpointRegistry kafkaListenerEndpointRegistry;


    @BeforeEach
    void setUp() {
        kafkaTestListener.trackingStatusCounter.set(0);

        // Ensure that the Kafka partitions are assigned to the listener containers
        kafkaListenerEndpointRegistry.getListenerContainers().forEach(container ->
                ContainerTestUtils.waitForAssignment(container, embeddedKafkaBroker.getPartitionsPerTopic()));

        log.info("KafkaTestListener set up complete. DispatchedPreparing counter reset to 0.");
    }

    @Test
    void testDispatchTrackingFlow() throws Exception {

        // This test would verify the end-to-end flow of dispatch tracking
        // by sending a DispatchPreparing event and checking if it is received by the Kafka listener.

        log.info("Starting DispatchTrackingIntegrationTest...");
        DispatchPreparing dispatchPreparing = TestEventData.buildDispatchPreparingEvent(UUID.randomUUID());

        log.info("Sending DispatchPreparing event: {}", dispatchPreparing);
        sendEventMessage(dispatchPreparing);

        log.info("Waiting for DispatchPreparing event to be processed...");
        // Wait for the listener to process the event
        await().atMost(10, TimeUnit.SECONDS)
                .until(() -> kafkaTestListener.trackingStatusCounter.get(), equalTo(1));

        log.info("Ending DispatchTrackingIntegrationTest...");

    }

    // for tests in the future, we can use this method to send messages to Kafka topics
    // we temporarily removed the topic parameter since we are only testing the dispatch tracking topic
    private void sendEventMessage(Object object) throws Exception {
        kafkaTemplate.send(MessageBuilder
                .withPayload(object)
                .setHeader(KafkaHeaders.TOPIC, DISPATCH_TRACKING_TOPIC)
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

        AtomicInteger trackingStatusCounter = new AtomicInteger(0);

        @KafkaListener(topics = TrackingService.TRACKING_TOPIC, groupId = "KafkaIntegrationTestGroup",
                containerFactory = "kafkaListenerContainerFactory")

        public void listen(final @Payload TrackingStatusUpdated payload) {
            log.info("Received TrackingStatus payload: {}", payload);
            trackingStatusCounter.incrementAndGet();

        }

    }
}
