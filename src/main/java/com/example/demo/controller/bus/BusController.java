package com.example.demo.controller.bus;

import com.example.demo.dto.bus.BusStationRequestDto;
import com.example.demo.response.Response;
import com.example.demo.service.bus.BusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BusController {

    private final BusService busService;

    @GetMapping("/bus")
    @ResponseStatus(HttpStatus.OK)
    public Response stationList(@RequestBody BusStationRequestDto requestDto) throws IOException {
        return Response.success(busService.getBusStationList(requestDto));
    }

    @GetMapping("/bus/{stationId}")
    @ResponseStatus(HttpStatus.OK)
    public Response arriveBusList(@PathVariable String stationId) throws IOException {
        return Response.success(busService.getBusArriveTimeList(stationId));
    }

    @GetMapping("/bus/{stationId}/{busNumber}")
    @ResponseStatus(HttpStatus.OK)
    public Response getBusRoute(@PathVariable String stationId, @PathVariable String busNumber) throws IOException {
        return Response.success(busService.getBusRoute(stationId, busNumber));
    }
}
