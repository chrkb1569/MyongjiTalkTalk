package com.example.demo.dto.bus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BusRouteSearchResultDto {
    private String stationId;
    private String stationSeq;
    private String stationName;
}