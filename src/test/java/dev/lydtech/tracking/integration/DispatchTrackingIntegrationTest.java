package dev.lydtech.tracking.integration;

import dev.lydtech.tracking.config.TrackingConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

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
}
