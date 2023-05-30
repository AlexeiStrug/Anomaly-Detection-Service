package com.example.demo.anomaly;

import com.example.demo.anomaly.dto.PageableRequest;
import com.example.demo.anomaly.dto.TemperatureThresholdDto;
import com.example.demo.anomaly.model.Anomaly;
import org.springframework.data.domain.Page;

public interface AnomalyService {

    Page<Anomaly> getAnomaliesByThermometerId(String thermometerId, PageableRequest pageableRequest);

    Page<Anomaly> getAnomaliesByRoomId(String roomId, PageableRequest pageableRequest);

    Page<TemperatureThresholdDto> getThermometersWithHighAnomalyCount(Integer threshold, PageableRequest pageableRequest);
}
