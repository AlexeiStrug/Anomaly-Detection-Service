package com.example.demo.anomaly.detector;

import com.example.demo.anomaly.detector.alghorithm.AnomalyAlgorithm;
import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithm;
import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithmOne;
import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithmTwo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AnomalyDetectorStrategyFactory {

    private final AnomalyDetectorAlgorithmOne anomalyDetectorOne;
    private final AnomalyDetectorAlgorithmTwo anomalyDetectorTwo;

    public AnomalyDetectorAlgorithm createStrategy(AnomalyAlgorithm algorithm) {
        return switch (algorithm) {
            case ONE -> anomalyDetectorOne;
            case TWO -> anomalyDetectorTwo;
            default -> throw new IllegalArgumentException("Invalid anomaly detection algorithm: " + algorithm);
        };
    }
}
