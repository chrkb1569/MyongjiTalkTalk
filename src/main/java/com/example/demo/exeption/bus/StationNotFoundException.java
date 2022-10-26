package com.example.demo.exeption.bus;

public class StationNotFoundException extends RuntimeException {

    public StationNotFoundException(String station) {
        super(station);
    }
}
