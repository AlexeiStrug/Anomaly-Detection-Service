package com.example.demo.anomaly;

import com.example.demo.anomaly.model.Anomaly;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

public interface AnomalySseService {

    SseEmitter register();

    void emitEvent(Anomaly anomaly);
}
