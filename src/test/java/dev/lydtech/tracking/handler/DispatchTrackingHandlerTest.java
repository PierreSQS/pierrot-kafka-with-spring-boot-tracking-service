package dev.lydtech.tracking.handler;

import dev.lydtech.dispatch.event.DispatchCompleted;
import dev.lydtech.dispatch.event.DispatchPreparing;
import dev.lydtech.tracking.service.TrackingService;
import dev.lydtech.tracking.util.TestEventData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class DispatchTrackingHandlerTest {

    private DispatchTrackingHandler dispatchTrackingHandler;
    private TrackingService trackingServMock;

    private DispatchPreparing dispatchPreparing;

    private DispatchCompleted dispatchCompleted;

    @BeforeEach
    void setUp() {
        dispatchPreparing = TestEventData.buildDispatchPreparingEvent(java.util.UUID.randomUUID());
        dispatchCompleted = TestEventData.buildDispatchCompletedEvent(dispatchPreparing.getOrderId());
        trackingServMock = mock(TrackingService.class);
        dispatchTrackingHandler = new DispatchTrackingHandler(trackingServMock);
    }

    @Test
    void listen_DispatchPreparing_Success() throws Exception {
        dispatchTrackingHandler.listenDispatchPreparing(dispatchPreparing);
        verify(trackingServMock).processDispatchPreparing(dispatchPreparing);
    }

    @Test
    void listen_DispatchCompleted_Success() throws Exception {
        dispatchTrackingHandler.listenDispatchCompleted(dispatchCompleted);
        verify(trackingServMock).processDispatchCompleted(dispatchCompleted);
    }

    @Test
    void listen_DispatchPreparing_ServiceThrowsException() throws Exception {
        // given
        // Mock the service to throw an exception when processing the event
        doThrow(new RuntimeException("Service failure")).when(trackingServMock).processDispatchPreparing(dispatchPreparing);

        // when
        dispatchTrackingHandler.listenDispatchPreparing(dispatchPreparing);

        // verify that the service was called
        verify(trackingServMock).processDispatchPreparing(dispatchPreparing);

    }

    @Test
    void listen_DispatchCompleted_ServiceThrowsException() throws Exception {
        // given
        // Mock the service to throw an exception when processing the event
        doThrow(new RuntimeException("Service failure"))
                .when(trackingServMock).processDispatchCompleted(dispatchCompleted);

        // when
        dispatchTrackingHandler.listenDispatchCompleted(dispatchCompleted);

        // verify that the service was called
        verify(trackingServMock).processDispatchCompleted(dispatchCompleted);
    }
}