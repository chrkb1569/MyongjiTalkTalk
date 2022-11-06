package com.example.demo.controller.weather;

import com.example.demo.response.Response;
import com.example.demo.service.weather.WeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping("/weather")
    @ResponseStatus(HttpStatus.OK)
    public Response getWeather() throws IOException {
        return Response.success(weatherService.getWeatherInfo());
    }
}
