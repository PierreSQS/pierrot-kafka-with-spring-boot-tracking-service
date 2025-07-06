package dev.lydtech.tracking.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TrackingService {

    public void process(String payload) {
        log.info("Processing tracking payload: {}", payload);


        log.info("Tracking information processed for payload: {}", payload);
    }
}
