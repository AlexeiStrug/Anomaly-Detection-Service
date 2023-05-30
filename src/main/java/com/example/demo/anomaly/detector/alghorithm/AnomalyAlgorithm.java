package com.example.demo.anomaly.detector.alghorithm;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

@AllArgsConstructor
public enum AnomalyAlgorithm {
    ONE("ONE"),
    TWO("TWO");

    @Getter
    private final String value;

    public static AnomalyAlgorithm fromValue(String provided) {
        return Arrays.stream(values()).filter(algorithm -> algorithm.value.equalsIgnoreCase(provided))
                .findFirst()
                .orElseThrow(() -> {
                    throw new IllegalArgumentException("Invalid anomaly detection algorithm: " + provided);
                });
    }
}
