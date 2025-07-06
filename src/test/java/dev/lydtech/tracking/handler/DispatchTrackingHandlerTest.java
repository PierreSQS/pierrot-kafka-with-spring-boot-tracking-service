package dev.lydtech.tracking.handler;

import dev.lydtech.tracking.service.TrackingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DispatchTrackingHandlerTest {

    private DispatchTrackingHandler dispatchTrackingHandler;
    private TrackingService trackingServMock;

    @BeforeEach
    void setUp() {
        trackingServMock = mock(TrackingService.class);
        dispatchTrackingHandler = new DispatchTrackingHandler(trackingServMock);
    }

    @Test
    void listen() {
        dispatchTrackingHandler.listen("test");
        verify(trackingServMock).process("test");
    }
}