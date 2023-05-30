package com.example.demo.anomaly;

import com.example.demo.anomaly.dto.PageableRequest;
import com.example.demo.anomaly.dto.TemperatureThresholdDto;
import com.example.demo.anomaly.model.Anomaly;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequestMapping("/v1/anomalies")
@RequiredArgsConstructor
@Validated
public class AnomalyController {

    private final AnomalyService anomalyService;
    private final AnomalySseService anomalySseService;

    @GetMapping("/thermometer/{thermometerId}")
    public Page<Anomaly> getAnomaliesByThermometerId(@PathVariable String thermometerId,
                                                     @Valid PageableRequest pageableRequest) {
        return anomalyService.getAnomaliesByThermometerId(thermometerId, pageableRequest);
    }

    @GetMapping("/room/{roomId}")
    public Page<Anomaly> getAnomaliesByRoomId(@PathVariable String roomId,
                                              @Valid PageableRequest pageableRequest) {
        return anomalyService.getAnomaliesByRoomId(roomId, pageableRequest);
    }

    @GetMapping("/high-threshold/{threshold}")
    public Page<TemperatureThresholdDto> getThermometersWithHighAnomalyCount(@PathVariable Integer threshold,
                                                                             @Valid PageableRequest pageableRequest) {
        return anomalyService.getThermometersWithHighAnomalyCount(threshold, pageableRequest);
    }

    @GetMapping("/stream")
    public SseEmitter streamAnomalies() {
        return anomalySseService.register();
    }
}
