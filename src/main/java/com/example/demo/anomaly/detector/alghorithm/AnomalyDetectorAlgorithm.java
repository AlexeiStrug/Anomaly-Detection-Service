package com.example.demo.anomaly.detector.alghorithm;

import com.example.demo.anomaly.measurement.TemperatureMeasurement;

import java.util.List;

public interface AnomalyDetectorAlgorithm {
    boolean isAnomaly(TemperatureMeasurement measurement, List<TemperatureMeasurement> recentMeasurements);

    void filterRecentMeasurements(List<TemperatureMeasurement> recentMeasurements);
}
