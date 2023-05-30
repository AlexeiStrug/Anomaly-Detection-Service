package com.example.demo.anomaly.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder(toBuilder = true)
public class PageableRequest {

    @Min(value = 0, message = "Page index must be greater than or equal to 0")
    @Builder.Default
    private int pageIndex = 0;

    @Min(value = 1, message = "Page size must be greater than or equal to 1")
    @Builder.Default
    private int pageSize = 100;
}
