package com.example.demo.dto.bus;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BusStationResponseDto {
    private String resultCode;
    private String totalCount;
    private List<BusStationSearchResultDto> items;
}