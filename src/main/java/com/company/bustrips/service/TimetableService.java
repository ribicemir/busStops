package com.company.bustrips.service;

import com.company.bustrips.model.Arrival;
import com.company.bustrips.model.StopTime;
import com.company.bustrips.parser.GtfsParser;

import java.io.IOException;
import java.time.LocalTime;
import java.util.*;

public class TimetableService {
    private final GtfsParser parser;
    public TimetableService(GtfsParser parser) {
        this.parser = parser;
    }

    public Optional<List<Arrival>> getUpcomingArrivals(String stationId, LocalTime now) throws IOException{
        Optional<String> stopName=parser.findStopName(stationId);
        if (stopName.isEmpty()){
            return Optional.empty();
        }

        List<StopTime> stopTimes=parser.findUpcomingStopTimes(stationId,now);
        if (stopTimes.isEmpty()) {
            return Optional.of(Collections.emptyList());
        }

        Set<String> tripIds = new HashSet<>();
        for (StopTime st : stopTimes) {
            tripIds.add(st.tripId());
        }

        Map<String, String> tripToRoute = parser.findRouteIdsForTrips(tripIds);

        Set<String> routeIds = new HashSet<>();
        for (String routeId : tripToRoute.values()) {
            routeIds.add(routeId);
        }
        Map<String, String> routeNames = parser.findRouteShortNames(routeIds);

        List<Arrival> arrivals = new ArrayList<>();
        for (StopTime st : stopTimes) {
            String routeId = tripToRoute.get(st.tripId());
            String lineName = routeNames.get(routeId);


            if (lineName == null) {
                lineName = "Neznana";
            }

            arrivals.add(new Arrival(lineName, st.arrivalTime(), st.minutesUntilArrival()));
        }

        arrivals.sort((a1, a2) -> a1.arrivalTime().compareTo(a2.arrivalTime()));
        return Optional.of(arrivals);


    }
}
