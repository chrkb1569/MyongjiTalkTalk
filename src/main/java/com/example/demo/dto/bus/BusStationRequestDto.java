package com.example.demo.dto.bus;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class BusStationRequestDto {
    private String stop_nm; // 버스 정류장의 정류장명
}
