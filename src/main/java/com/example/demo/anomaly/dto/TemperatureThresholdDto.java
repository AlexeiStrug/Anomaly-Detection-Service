package com.example.demo.anomaly.dto;

import lombok.Builder;

@Builder(toBuilder = true)
public record TemperatureThresholdDto(String thermometerId,
                                      Long anomalyCount) {
}
