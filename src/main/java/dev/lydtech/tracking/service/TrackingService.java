package dev.lydtech.tracking.service;

import dev.lydtech.tracking.event.DispatchPreparing;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class TrackingService {

    public void process(DispatchPreparing payload) {
        log.info("Processing tracking payload: {}", payload);


        log.info("Tracking information processed for payload: {}", payload);
    }
}
