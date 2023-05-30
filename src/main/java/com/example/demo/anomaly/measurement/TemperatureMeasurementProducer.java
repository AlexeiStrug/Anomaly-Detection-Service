package com.example.demo.anomaly.measurement;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TemperatureMeasurementProducer {

    private final KafkaTemplate<String, TemperatureMeasurement> kafkaTemplate;

    public void sendTemperatureMeasurement(TemperatureMeasurement measurement) {
        this.kafkaTemplate.send("temperature-measurements", measurement);
    }
}
