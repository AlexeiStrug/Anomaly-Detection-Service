package com.example.demo.anomaly.impl;

import com.example.demo.anomaly.AnomalyRepository;
import com.example.demo.anomaly.dto.PageableRequest;
import com.example.demo.anomaly.dto.TemperatureThresholdDto;
import com.example.demo.anomaly.impl.AnomalyServiceImpl;
import com.example.demo.anomaly.model.Anomaly;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AnomalyServiceTest {
    @Mock
    private AnomalyRepository anomalyRepository;

    @InjectMocks
    private AnomalyServiceImpl anomalyService;

    @Test
    void shouldReturnAnomaliesByThermometerId_whenOneAnomalyExists() {
        // Arrange
        final var thermometerId = "Thermometer1";
        final var anomaly = new Anomaly().toBuilder()
                .thermometerId(thermometerId)
                .build();
        final var pageableRequest = getPageableRequest();
        final var pageable = getPageRequest();

        List<Anomaly> anomalies = Collections.singletonList(anomaly);
        Page<Anomaly> page = new PageImpl<>(anomalies, pageable, anomalies.size());

        when(anomalyRepository.findByThermometerId(eq(thermometerId), any(Pageable.class))).thenReturn(page);

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByThermometerId(thermometerId, pageableRequest);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(thermometerId, result.getContent().get(0).getThermometerId());
    }

    @Test
    void shouldReturnEmpty_whenNoAnomaliesExist() {
        // Arrange
        final var thermometerId = "Thermometer1";
        final var pageableRequest = getPageableRequest();

        when(anomalyRepository.findByThermometerId(eq(thermometerId), any(Pageable.class))).thenReturn(Page.empty());

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByThermometerId(thermometerId, pageableRequest);

        // Assert
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void shouldReturnEmpty_whenThermometerIdNull() {
        // Arrange
        final String thermometerId = null;
        final var pageableRequest = getPageableRequest();

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByThermometerId(thermometerId, pageableRequest);

        // Assert
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void shouldReturnAnomalies_whenMultipleAnomaliesExist() {
        // Arrange
        final var thermometerId = "Thermometer1";
        final var anomaly1 = new Anomaly().toBuilder().thermometerId(thermometerId).build();
        final var anomaly2 = new Anomaly().toBuilder().thermometerId(thermometerId).build();

        final var pageableRequest = getPageableRequest();
        final var pageable = getPageRequest();

        List<Anomaly> anomalies = Arrays.asList(anomaly1, anomaly2);
        Page<Anomaly> page = new PageImpl<>(anomalies, pageable, anomalies.size());

        when(anomalyRepository.findByThermometerId(eq(thermometerId), any(Pageable.class))).thenReturn(page);

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByThermometerId(thermometerId, pageableRequest);

        // Assert
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(a -> a.getThermometerId().equals(thermometerId)));
    }

    @Test
    public void shouldReturnAnomaliesByRoomId_whenOneAnomalyExists() {
        // Arrange
        final var roomId = "Room1";
        final var anomaly = new Anomaly().toBuilder()
                .roomId(roomId)
                .build();
        final var pageableRequest = getPageableRequest();
        final var pageable = getPageRequest();

        List<Anomaly> anomalies = Collections.singletonList(anomaly);
        Page<Anomaly> page = new PageImpl<>(anomalies, pageable, anomalies.size());

        when(anomalyRepository.findByRoomId(eq(roomId), any(Pageable.class))).thenReturn(page);

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByRoomId(roomId, pageableRequest);

        // Assert
        assertEquals(1, result.getContent().size());
        assertEquals(roomId, result.getContent().get(0).getRoomId());
    }

    @Test
    public void shouldReturnEmptyByRoomId_whenNoAnomaliesExist() {
        // Arrange
        final var roomId = "Room1";
        final var pageableRequest = getPageableRequest();

        when(anomalyRepository.findByRoomId(roomId, PageRequest.of(0, 10))).thenReturn(Page.empty());

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByRoomId(roomId, pageableRequest);

        // Assert
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void shouldReturnEmptyByRoomId_whenThermometerIdNull() {
        // Arrange
        final String roomId = null;
        final var pageableRequest = getPageableRequest();

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByRoomId(roomId, pageableRequest);

        // Assert
        assertTrue(result.getContent().isEmpty());
    }

    @Test
    public void shouldReturnAnomaliesByRoomId_whenMultipleAnomaliesExist() {
        // Arrange
        final var roomId = "Room1";
        final var pageableRequest = getPageableRequest();
        final var anomalies = IntStream.rangeClosed(1, 10)
                .mapToObj(i -> new Anomaly((long) i, "Thermometer" + i, roomId, 0L, 0.0))
                .collect(Collectors.toList());
        final var pagedResponse = new PageImpl<>(anomalies, PageRequest.of(0, 10), anomalies.size());

        when(anomalyRepository.findByRoomId(roomId, PageRequest.of(0, 10))).thenReturn(pagedResponse);

        // Act
        Page<Anomaly> result = anomalyService.getAnomaliesByRoomId(roomId, pageableRequest);

        // Assert
        assertEquals(10, result.getContent().size());
        verify(anomalyRepository, times(1)).findByRoomId(roomId, PageRequest.of(0, 10));
    }

//    @Test
//    public void shouldReturnThermometersWithHighAnomalyCount() {
//        // Arrange
//        var threshold = 2;
//        var pageableRequest = new PageableRequest(0, 10);
//        var anomalyCounts = new Object[] {"Thermometer1", 3};
//        var pagedResponse = new PageImpl<>(List.of(anomalyCounts), PageRequest.of(0, 10), 1);
//
//        when(anomalyRepository.findThermometersWithHighAnomalyCount(threshold, PageRequest.of(0, 10))).thenReturn(pagedResponse);
//
//        // Act
//        Page<Anomaly> result = anomalyService.getThermometersWithHighAnomalyCount(threshold, pageableRequest);
//
//        // Assert
//        assertEquals(1, result.getContent().size());
//        assertEquals("Thermometer1", result.getContent().get(0).thermometerId());
//        assertEquals(3, result.getContent().get(0).anomalyCount());
//        verify(anomalyRepository, times(1)).findThermometersWithHighAnomalyCount(threshold, PageRequest.of(0, 10));
//    }

    @Test
    void getThermometersWithHighAnomalyCount_WithValidThreshold_ReturnsNonEmptyPage() {
        // Arrange
        final var threshold = 5;
        final var pageableRequest = getPageableRequest();

        List<Object[]> expectedResults = new ArrayList<>();
        expectedResults.add(new Object[]{"Thermometer1", 7L});
        expectedResults.add(new Object[]{"Thermometer2", 6L});
        Page<Object[]> expectedResult = new PageImpl<>(expectedResults);

        when(anomalyRepository.findThermometersWithHighAnomalyCount(threshold, PageRequest.of(0, 10))).thenReturn(expectedResult);

        // Act
        Page<TemperatureThresholdDto> result = anomalyService.getThermometersWithHighAnomalyCount(threshold, pageableRequest);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(2, result.getNumberOfElements());

        assertEquals("Thermometer1", result.getContent().get(0).thermometerId());
        assertEquals(7, result.getContent().get(0).anomalyCount());

        assertEquals("Thermometer2", result.getContent().get(1).thermometerId());
        assertEquals(6, result.getContent().get(1).anomalyCount());
    }

    @Test
    void getThermometersWithHighAnomalyCount_WithNullThreshold_ReturnsEmptyPage() {
        // Arrange
        final Integer threshold = null;
        final var pageableRequest = new PageableRequest();

        // Act
        Page<TemperatureThresholdDto> result = anomalyService.getThermometersWithHighAnomalyCount(threshold, pageableRequest);

        // Assert
        assertTrue(result.isEmpty());
        verify(anomalyRepository, never()).findThermometersWithHighAnomalyCount(anyInt(), any(Pageable.class));
    }

    private static PageableRequest getPageableRequest() {
        return new PageableRequest().toBuilder()
                .pageIndex(0)
                .pageSize(10)
                .build();
    }

    @NotNull
    private static PageRequest getPageRequest() {
        return PageRequest.of(0, 10);
    }

}
