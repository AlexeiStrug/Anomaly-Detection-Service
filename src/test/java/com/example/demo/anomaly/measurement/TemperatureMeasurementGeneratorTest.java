package com.example.demo.anomaly.measurement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Random;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class TemperatureMeasurementGeneratorTest {

    @InjectMocks
    private TemperatureMeasurementGenerator temperatureMeasurementGenerator;

    @Mock
    private TemperatureMeasurementProducer temperatureMeasurementProducer;

    private Random mockRandom;

    @BeforeEach
    void setUp() {
        mockRandom = new Random() {
            @Override
            public double nextDouble() {
                return 0.5; // controlled output
            }

            @Override
            public int nextInt(int bound) {
                return 5; // controlled output
            }
        };
        ReflectionTestUtils.setField(temperatureMeasurementGenerator, "random", mockRandom);
    }

    @Test
    void generateMeasurement() {
        temperatureMeasurementGenerator.generateMeasurement();

        final var expectedMeasurements = 1000;
        verify(temperatureMeasurementProducer, times(expectedMeasurements)).sendTemperatureMeasurement(any(TemperatureMeasurement.class));
    }
}
