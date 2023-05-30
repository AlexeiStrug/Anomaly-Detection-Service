package com.example.demo.anomaly.impl;

import com.example.demo.anomaly.AnomalyRepository;
import com.example.demo.anomaly.AnomalyService;
import com.example.demo.anomaly.dto.PageableRequest;
import com.example.demo.anomaly.dto.TemperatureThresholdDto;
import com.example.demo.anomaly.model.Anomaly;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AnomalyServiceImpl implements AnomalyService {

    private final AnomalyRepository anomalyRepository;

    @Override
    public Page<Anomaly> getAnomaliesByThermometerId(String thermometerId, PageableRequest pageableRequest) {
        if (thermometerId == null || thermometerId.isBlank()) {
            return Page.empty();
        }
        final var pageable = generatePageRequest(pageableRequest);

        return anomalyRepository.findByThermometerId(thermometerId, pageable);
    }

    @Override
    public Page<Anomaly> getAnomaliesByRoomId(String roomId, PageableRequest pageableRequest) {
        if (roomId == null || roomId.isBlank()) {
            return Page.empty();
        }
        final var pageable = generatePageRequest(pageableRequest);

        return anomalyRepository.findByRoomId(roomId, pageable);
    }

    @Override
    public Page<TemperatureThresholdDto> getThermometersWithHighAnomalyCount(Integer threshold, PageableRequest pageableRequest) {
        if (threshold == null) {
            return Page.empty();
        }

        final var pageable = generatePageRequest(pageableRequest);
        final Page<Object[]> result = anomalyRepository.findThermometersWithHighAnomalyCount(threshold, pageable);

        if (result.isEmpty()) {
            return Page.empty();
        }

        return result.map(row -> {
            final String thermometerId = (String) row[0];
            final Long anomalyCount = (Long) row[1];
            return new TemperatureThresholdDto(thermometerId, anomalyCount);
        });
    }

    private Pageable generatePageRequest(PageableRequest pageableRequest) {
        final int page = pageableRequest.getPageIndex();
        final int size = pageableRequest.getPageSize();

        return PageRequest.of(page, size);
    }
}
