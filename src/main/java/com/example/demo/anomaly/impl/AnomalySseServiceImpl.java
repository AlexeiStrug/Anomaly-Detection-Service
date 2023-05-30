package com.example.demo.anomaly.impl;

import com.example.demo.anomaly.AnomalySseService;
import com.example.demo.anomaly.model.Anomaly;
import lombok.Getter;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.concurrent.CopyOnWriteArrayList;

@Service
public class AnomalySseServiceImpl implements AnomalySseService {

    @Getter
    private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();

    @Override
    public SseEmitter register() {
        final var emitter = new SseEmitter();
        this.emitters.add(emitter);

        emitter.onTimeout(() -> this.emitters.remove(emitter));
        emitter.onCompletion(() -> this.emitters.remove(emitter));

        return emitter;
    }

    @Override
    public void emitEvent(Anomaly anomaly) {
        final var deadEmitters = emitters
                .stream()
                .filter(emitter -> {
                    try {
                        emitter.send(anomaly);
                        return false;
                    } catch (Exception e) {
                        return true;
                    }
                })
                .toList();

        this.emitters.removeAll(deadEmitters);
    }
}
