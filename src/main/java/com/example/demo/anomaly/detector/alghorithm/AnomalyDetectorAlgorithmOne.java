package com.example.demo.anomaly.detector.alghorithm;

import com.example.demo.anomaly.measurement.TemperatureMeasurement;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Stream;

@Service
public class AnomalyDetectorAlgorithmOne implements AnomalyDetectorAlgorithm {
    private static final int MAX_MEASUREMENTS = 10;
    private static final double ANOMALY_THRESHOLD = 5.0;

    @Override
    public boolean isAnomaly(TemperatureMeasurement measurement, List<TemperatureMeasurement> recentMeasurements) {
        // Check if there are enough measurements for analysis
        if (recentMeasurements.size() < MAX_MEASUREMENTS) {
            return false;
        }

        // Get the last 10 measurements
        List<TemperatureMeasurement> lastMeasurements = recentMeasurements.subList(recentMeasurements.size() - MAX_MEASUREMENTS, recentMeasurements.size());

        // Calculate the average of the last 9 measurements for each measurement and check if it is an anomaly
        for (int i = 0; i < MAX_MEASUREMENTS; i++) {
            double avg = Stream.concat(lastMeasurements.subList(0, i).stream(), lastMeasurements.subList(i + 1, MAX_MEASUREMENTS).stream())
                    .mapToDouble(TemperatureMeasurement::getTemperature)
                    .average()
                    .orElse(Double.NaN);

            if (lastMeasurements.get(i).getTemperature() > avg + ANOMALY_THRESHOLD) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void filterRecentMeasurements(List<TemperatureMeasurement> recentMeasurements) {
        while (recentMeasurements.size() > MAX_MEASUREMENTS) {
            recentMeasurements.remove(0);
        }
    }
}
