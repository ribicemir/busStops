package com.company.bustrips;

import com.company.bustrips.cli.AppArgs;
import com.company.bustrips.model.Arrival;
import com.company.bustrips.parser.GtfsParser;
import com.company.bustrips.service.TimetableService;
import com.company.bustrips.util.TimeUtils;

import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        try {
            AppArgs appArgs = AppArgs.parse(args);
            GtfsParser parser = new GtfsParser(Path.of("data"));
            TimetableService service = new TimetableService(parser);

            LocalTime now = LocalTime.now();
            Optional<List<Arrival>> upcomingArrivals = service.getUpcomingArrivals(appArgs.stationId(), now);

            if (upcomingArrivals.isEmpty()) {
                System.out.println("Postaja z ID " + appArgs.stationId() + " ne obstaja.");
                return;
            }

            Optional<String> stopNameOpt = parser.findStopName(appArgs.stationId());
            System.out.println(stopNameOpt.orElse("Neznana postaja"));

            List<Arrival> arrivals = upcomingArrivals.get();
            if (arrivals.isEmpty()) {
                return;
            }

            Map<String, List<Arrival>> grouped = new TreeMap<>();
            for (Arrival a : arrivals) {
                grouped.computeIfAbsent(a.shortNameofRoute(), k -> new ArrayList<>()).add(a);
            }

            for (Map.Entry<String, List<Arrival>> entry : grouped.entrySet()) {
                String lineName = entry.getKey();
                List<Arrival> lineArrivals = entry.getValue();

                String timesStr = lineArrivals.stream()
                        .limit(appArgs.numBusesPerLine())
                        .map(a -> TimeUtils.formatTime(now, a.arrivalTime(), appArgs.timeFormat()))
                        .collect(Collectors.joining(", "));

                System.out.println(lineName + ": " + timesStr);
            }

        } catch (IllegalArgumentException e) {
            System.err.println("Napaka: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Prišlo je do sistemske napake: " + e.getMessage());
        }
    }
}