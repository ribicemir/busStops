package com.company.bustrips.model;
import java.time.LocalTime;
public record Arrival(
        String shortNameofRoute,
        LocalTime arrivalTime,
        long minUntilArrival
) {}
