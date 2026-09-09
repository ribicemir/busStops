package com.company.bustrips;

import com.company.bustrips.model.Arrival;
import com.company.bustrips.parser.GtfsParser;
import com.company.bustrips.service.TimetableService;
import com.company.bustrips.util.TimeUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.*;

public class Main {
    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Uporaba: busTrips <station_id> <num_buses_per_line> <relative|absolute>");
            return;
        }
        String stationId = args[0];
        int numBusesPerLine;
        try {
            numBusesPerLine = Integer.parseInt(args[1].trim());
            if (numBusesPerLine <= 0) {
                System.err.println("Napaka: num_buses_per_line mora biti pozitivno število.");
                return;
            }
        } catch (NumberFormatException e) {
            System.err.println("Napaka: num_buses_per_line mora biti veljavno celo število.");
            return;
        }
        String formatType = args[2].trim().toLowerCase();
        if (!formatType.equals("relative") && !formatType.equals("absolute")) {
            System.err.println("Napaka: Format mora biti 'relative' ali 'absolute'.");
            return;
        }
        Path dataDir = Path.of("data");
        if (!Files.exists(dataDir)) {
            System.err.println("Napaka: Mapa 'data/' s podatki GTFS ne obstaja v delovnem imeniku.");
            return;
        }

        try {
            GtfsParser parser = new GtfsParser(dataDir);
            TimetableService service = new TimetableService(parser);

            Optional<String> stopNameOpt = parser.findStopName(stationId);
            if (stopNameOpt.isEmpty()) {
                System.out.println("Postaja z ID " + stationId + " ne obstaja.");
                return;
            }

            LocalTime now = LocalTime.now();
            Optional<List<Arrival>> arrivalsOpt = service.getUpcomingArrivals(stationId, now);
            List<Arrival> arrivals = arrivalsOpt.orElse(Collections.emptyList());

            Map<String, List<Arrival>> arrivalsByRoute = new TreeMap<>();
            for (Arrival a : arrivals) {
                String route = a.shortNameofRoute();
                if (!arrivalsByRoute.containsKey(route)) {
                    arrivalsByRoute.put(route, new ArrayList<>());
                }
                arrivalsByRoute.get(route).add(a);
            }


            System.out.println(stopNameOpt.get());

            if (arrivalsByRoute.isEmpty()) {
                System.out.println("V naslednjih 2 urah ni prihodov.");
                return;
            }

            for (Map.Entry<String, List<Arrival>> entry : arrivalsByRoute.entrySet()) {
                String route = entry.getKey();
                List<Arrival> routeArrivals = entry.getValue();

                List<String> formattedTimes = new ArrayList<>();
                for (int i = 0; i < routeArrivals.size() && i < numBusesPerLine; i++) {
                    Arrival a = routeArrivals.get(i);
                    String formatted = TimeUtils.formatTime(now, a.arrivalTime(), formatType);
                    formattedTimes.add(formatted);
                }

                System.out.println(route + ": " + String.join(", ", formattedTimes));
            }

        } catch (IOException e) {
            System.err.println("Napaka pri branju datotek: " + e.getMessage());
        }
    }
}
