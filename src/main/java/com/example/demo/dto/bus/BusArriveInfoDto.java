package com.example.demo.dto.bus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusArriveInfoDto {
    private String stationId;
    private String routeId;
    private String predictTime1;
    private String predictTime2;
}