package com.example.demo.anomaly.detector.algorithm;

import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithmOne;
import com.example.demo.anomaly.detector.alghorithm.AnomalyDetectorAlgorithmTwo;
import com.example.demo.anomaly.measurement.TemperatureMeasurement;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AnomalyDetectorAlgorithmTest {
    @InjectMocks
    private AnomalyDetectorAlgorithmOne algorithmOne;

    @InjectMocks
    private AnomalyDetectorAlgorithmTwo algorithmTwo;

    @Test
    void isAnomaly_AlgorithmOne_shouldReturnTrue() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        for (int i = 0; i < 9; i++) {
            recentMeasurements.add(new TemperatureMeasurement(20, System.currentTimeMillis() - (i * 1000), "Room1", "Thermometer1"));
        }

        double anomalyTemp = 20 + 5.0 + 0.1; // Adding 0.1 to ensure the temperature is strictly more than the average + threshold

        recentMeasurements.add(new TemperatureMeasurement(anomalyTemp, System.currentTimeMillis() - 1000, "Room1", "Thermometer1"));
        TemperatureMeasurement measurement = new TemperatureMeasurement(30, System.currentTimeMillis(), "Room1", "Thermometer1");

        boolean result = algorithmOne.isAnomaly(measurement, recentMeasurements);
        assertTrue(result);
    }

    @Test
    void isAnomaly_AlgorithmOne_shouldReturnFalse_whenSizeLessThan10() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        recentMeasurements.add(new TemperatureMeasurement(20, System.currentTimeMillis(), "Room1", "Thermometer1"));

        boolean result = algorithmOne.isAnomaly(recentMeasurements.get(0), recentMeasurements);
        assertFalse(result);
    }

    @Test
    void isAnomaly_AlgorithmTwo_shouldReturnTrue() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            recentMeasurements.add(new TemperatureMeasurement(20, System.currentTimeMillis() - (i * 1000), "Room1", "Thermometer1"));
        }
        TemperatureMeasurement measurement = new TemperatureMeasurement(30, System.currentTimeMillis(), "Room1", "Thermometer1");

        boolean result = algorithmTwo.isAnomaly(measurement, recentMeasurements);
        assertTrue(result);
    }

    @Test
    void filterRecentMeasurements_AlgorithmOne_shouldReturnProperSize() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            recentMeasurements.add(new TemperatureMeasurement(20, System.currentTimeMillis() - (i * 1000), "Room1", "Thermometer1"));
        }

        algorithmOne.filterRecentMeasurements(recentMeasurements);
        assertEquals(10, recentMeasurements.size());
    }

    @Test
    void filterRecentMeasurements_AlgorithmTwo_shouldReturnProperSize() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        for (int i = 0; i < 20; i++) {
            recentMeasurements.add(new TemperatureMeasurement(20, currentTime - (19 - i) * 1000, "Room1", "Thermometer1"));
        }
        // Make sure measurements are sorted in ascending order of timestamp
        recentMeasurements.sort(Comparator.comparing(TemperatureMeasurement::getTimestamp));

        algorithmTwo.filterRecentMeasurements(recentMeasurements);

        // Check the size
        assertEquals(10, recentMeasurements.size());

        // Check that the oldest measurement in the list is no older than 10 seconds compared to the most recent measurement
        assertTrue(recentMeasurements.get(recentMeasurements.size() - 1).getTimestamp() - recentMeasurements.get(0).getTimestamp() <= 10000);
    }

    @Test
    void isAnomaly_AlgorithmOne_WithAnomaly_shouldReturnTrue() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        double[] temperatures = {20.1, 21.2, 20.3, 19.1, 20.1, 19.2, 20.1, 18.1, 19.4, 27.1, 23.1};
        for (double temp : temperatures) {
            recentMeasurements.add(new TemperatureMeasurement(temp, System.currentTimeMillis(), "Room1", "Thermometer1"));
        }

        boolean result = algorithmOne.isAnomaly(recentMeasurements.get(recentMeasurements.size() - 1), recentMeasurements);
        assertTrue(result);
    }

    @Test
    void isAnomaly_AlgorithmTwo_WithoutAnomaly_shouldReturnFalse() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        double[] temperatures = {19.1, 19.2, 19.5, 19.7, 19.3, 24.1, 18.2, 19.1, 19.2, 23.4};
        for (int i = 0; i < temperatures.length; i++) {
            TemperatureMeasurement newMeasurement = new TemperatureMeasurement(temperatures[i], currentTime + i, "Room1", "Thermometer1");
            recentMeasurements.add(newMeasurement);

            boolean isAnomaly = algorithmTwo.isAnomaly(newMeasurement, recentMeasurements);

            assertFalse(isAnomaly);
        }
    }

    @Test
    void isAnomaly_AlgorithmTwo_WithAnomaly_shouldReturnTrue() {
        List<TemperatureMeasurement> recentMeasurements = new ArrayList<>();
        long currentTime = System.currentTimeMillis();
        double[] temperatures = {19.1, 19.2, 19.5, 19.7, 19.3, 24.1, 18.2, 19.1, 19.2, 23.4};
        for (int i = 0; i < temperatures.length; i++) {
            recentMeasurements.add(new TemperatureMeasurement(temperatures[i], currentTime + (9 - i) * 1000, "Room1", "Thermometer1"));
        }
        // Add a temperature measurement that is at least 5 degrees higher than the average
        TemperatureMeasurement anomalyMeasurement = new TemperatureMeasurement(25.2, currentTime + 10 * 1000, "Room1", "Thermometer1");

        boolean result = algorithmTwo.isAnomaly(anomalyMeasurement, recentMeasurements);
        assertTrue(result);
    }

}
