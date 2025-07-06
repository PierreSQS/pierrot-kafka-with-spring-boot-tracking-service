package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.event.DispatchPreparing;
import dev.lydtech.tracking.service.TrackingService;
import dev.lydtech.tracking.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DispatchTrackingHandlerTest {

    private DispatchTrackingHandler dispatchTrackingHandler;
    private TrackingService trackingServMock;

    private DispatchPreparing dispatchPreparing;

    @BeforeEach
    void setUp() {
        dispatchPreparing = TestEventData.buildDispatchPreparingEvent(java.util.UUID.randomUUID());
        trackingServMock = mock(TrackingService.class);
        dispatchTrackingHandler = new DispatchTrackingHandler(trackingServMock);
    }

    @Test
    void listen() {
        dispatchTrackingHandler.listen(dispatchPreparing);
        verify(trackingServMock).process(dispatchPreparing);
    }
}