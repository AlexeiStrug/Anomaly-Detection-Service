package com.example.demo.anomaly.measurement;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@RequiredArgsConstructor
public class TemperatureMeasurementGenerator {

    private static final int MAX_MEASUREMENTS_PER_SECOND = 20000;
    private final Random random = new Random();
    private final TemperatureMeasurementProducer temperatureMeasurementProducer;

    /**
     * Generate a random temperature measurement with next values
     * 1. Temperature between 15 and 25
     * 2. Current time
     * 3. Room1 to Room10
     * 4. Thermometer1 to Thermometer100
     *
     * @return TemperatureMeasurement
     */
    @Scheduled(fixedRate = 50) // Adjust the rate based on desired measurements per second
    public void generateMeasurement() {
        for (int i = 0; i < MAX_MEASUREMENTS_PER_SECOND / 20; i++) {
            final var temperatureMeasurement = new TemperatureMeasurement().toBuilder()
                    .temperature(15 + random.nextDouble() * 10)
                    .timestamp(System.currentTimeMillis())
                    .roomId("Room" + (random.nextInt(10) + 1))
                    .thermometerId("Thermometer" + (random.nextInt(100) + 1))
                    .build();

            temperatureMeasurementProducer.sendTemperatureMeasurement(temperatureMeasurement);
        }
    }
}
