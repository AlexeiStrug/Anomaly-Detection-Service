package com.example.demo.anomaly.detector;

import com.example.demo.anomaly.AnomalyRepository;
import com.example.demo.anomaly.AnomalySseService;
import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithm;
import com.example.demo.anomaly.measurement.TemperatureMeasurement;
import com.example.demo.anomaly.model.Anomaly;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.LinkedList;

@RequiredArgsConstructor
public class TemperatureAnomalyAnalyzer {

    private final LinkedList<TemperatureMeasurement> recentMeasurements = new LinkedList<>();
    private final AnomalyRepository anomalyRepository;
    private final AnomalyDetectorAlgorithm anomalyDetector;
    private final AnomalySseService anomalySseService;

    @KafkaListener(topics = "temperature-measurements", groupId = "group_id")
    public void consume(TemperatureMeasurement measurement) {
        // Add the new measurement to the list
        recentMeasurements.addLast(measurement);

        // Ensure we only keep the required measurements based on the strategy
        anomalyDetector.filterRecentMeasurements(recentMeasurements);

        // Now perform the anomaly detection
        boolean isAnomaly = anomalyDetector.isAnomaly(measurement, recentMeasurements);

        if (isAnomaly) {
            final var anomaly = buildAnomaly(measurement);
            anomalyRepository.save(anomaly);
            anomalySseService.emitEvent(anomaly);
        }
    }

    private Anomaly buildAnomaly(TemperatureMeasurement measurement) {
        return new Anomaly().toBuilder()
                .thermometerId(measurement.getThermometerId())
                .timestamp(measurement.getTimestamp())
                .roomId(measurement.getRoomId())
                .temperature(measurement.getTemperature())
                .build();
    }
}
