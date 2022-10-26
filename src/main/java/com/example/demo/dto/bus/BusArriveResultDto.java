package com.example.demo.dto.bus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class BusArriveResultDto {
    private String busNumber;
    private String arriveTime1;
    private String arriveTime2;

    public BusArriveResultDto toDto(BusArriveInfoDto listDto) {
        return new BusArriveResultDto(listDto.getRouteId(), listDto.getPredictTime1(), listDto.getPredictTime2());
    }
}
