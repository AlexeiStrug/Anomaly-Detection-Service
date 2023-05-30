package com.example.demo.anomaly.impl;

import com.example.demo.anomaly.model.Anomaly;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnomalySseServiceTest {

    @InjectMocks
    private AnomalySseServiceImpl anomalySseService;

    private SseEmitter mockEmitter1;
    private SseEmitter mockEmitter2;

    @BeforeEach
    void setUp() {
        mockEmitter1 = mock(SseEmitter.class);
        mockEmitter2 = mock(SseEmitter.class);
    }

    @Test
    void register() {
        SseEmitter emitter = anomalySseService.register();
        Assertions.assertNotNull(emitter);

        // Verify that the emitter is added to the emitters list
        Assertions.assertTrue(anomalySseService.getEmitters().contains(emitter));
    }

    @Test
    void emitEvent() throws IOException {
        AnomalySseServiceImpl anomalySseService = new AnomalySseServiceImpl();

        anomalySseService.getEmitters().add(mockEmitter1);
        anomalySseService.getEmitters().add(mockEmitter2);

        Anomaly anomaly = new Anomaly(); // Replace with an appropriate instance
        anomalySseService.emitEvent(anomaly);

        verify(mockEmitter1, times(1)).send(anomaly);
        verify(mockEmitter2, times(1)).send(anomaly);
    }


    @Test
    void emitEvent_removesDeadEmitters() throws IOException {
        AnomalySseServiceImpl anomalySseService = new AnomalySseServiceImpl();

        anomalySseService.getEmitters().add(mockEmitter1);
        anomalySseService.getEmitters().add(mockEmitter2);

        doThrow(new IOException()).when(mockEmitter2).send(any());

        Anomaly anomaly = new Anomaly();
        anomalySseService.emitEvent(anomaly);

        // Verify that the dead emitter is removed from the emitters list
        Assertions.assertFalse(anomalySseService.getEmitters().contains(mockEmitter2));
    }
}

