package com.example.demo.dto.weather;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class WeatherResultDto {
    private String preDate;
    private String preTime;
    private String temperature;
    private String rainFall;
    private String rainType;
    private String skyCondition;
    private String humidity;
}
