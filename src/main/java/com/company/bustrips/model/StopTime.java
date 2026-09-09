package com.company.bustrips.model;

import java.time.LocalTime;

public record StopTime(
    String tripId,
    LocalTime arrivalTime,
    long minutesUntilArrival
){}

