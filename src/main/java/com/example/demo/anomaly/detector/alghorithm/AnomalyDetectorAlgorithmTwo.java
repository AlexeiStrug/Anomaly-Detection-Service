package com.example.demo.anomaly.detector.alghorithm;

import com.example.demo.anomaly.measurement.TemperatureMeasurement;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AnomalyDetectorAlgorithmTwo implements AnomalyDetectorAlgorithm {

    private static final int TIME_WINDOW_MS = 10000;
    private static final double ANOMALY_THRESHOLD = 5.0;

    @Override
    public boolean isAnomaly(TemperatureMeasurement measurement, List<TemperatureMeasurement> recentMeasurements) {
        // Filter the measurements in the time window
        final var measurementsInWindow = recentMeasurements.stream()
                .filter(m -> m.getTimestamp() >= measurement.getTimestamp() - TIME_WINDOW_MS && m.getTimestamp() < measurement.getTimestamp())
                .toList();

        // Calculate the average of the measurements in the time window
        final double avg = measurementsInWindow.stream()
                .mapToDouble(TemperatureMeasurement::getTemperature)
                .average()
                .orElse(Double.NaN);

        // Check if the current measurement is an anomaly
        return measurement.getTemperature() > avg + ANOMALY_THRESHOLD;
    }


    @Override
    public void filterRecentMeasurements(List<TemperatureMeasurement> recentMeasurements) {
        if (!recentMeasurements.isEmpty()) {
            final long oldestAllowedTimestamp = recentMeasurements.get(recentMeasurements.size() - 1).getTimestamp() - TIME_WINDOW_MS;
            while (!recentMeasurements.isEmpty() && recentMeasurements.get(0).getTimestamp() <= oldestAllowedTimestamp) {
                recentMeasurements.remove(0);
            }
        }
    }
}
