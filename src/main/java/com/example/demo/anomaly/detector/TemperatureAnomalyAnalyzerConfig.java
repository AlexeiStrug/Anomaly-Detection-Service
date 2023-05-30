package com.example.demo.anomaly.detector;

import com.example.demo.anomaly.AnomalyRepository;
import com.example.demo.anomaly.AnomalySseService;
import com.example.demo.anomaly.detector.alghorithm.AnomalyAlgorithm;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

@Configuration
@RequiredArgsConstructor
public class TemperatureAnomalyAnalyzerConfig {

    private final AnomalyRepository anomalyRepository;
    private final AnomalyDetectorStrategyFactory strategyFactory;
    private final AnomalySseService anomalySseService;
    private final Environment env;

    @Bean
    public TemperatureAnomalyAnalyzer customTemperatureAnomalyAnalyzer() {
        final var algorithm = AnomalyAlgorithm.fromValue(env.getProperty("anomaly.detection.algorithm"));

        final var strategy = strategyFactory.createStrategy(algorithm);

        return new TemperatureAnomalyAnalyzer(anomalyRepository, strategy, anomalySseService);
    }
}
