package com.example.demo.anomaly.model;

import lombok.Builder;

@Builder(toBuilder = true)
public record AnomalyEvent(Anomaly anomaly) {
}
