package com.example.demo.anomaly.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class Anomaly {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;
    private String thermometerId;
    private String roomId;
    private long timestamp;
    private double temperature;
}
