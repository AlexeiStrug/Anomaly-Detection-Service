package com.example.demo.anomaly.measurement;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class TemperatureMeasurement {
    private double temperature;
    private long timestamp;
    private String roomId;
    private String thermometerId;
}
