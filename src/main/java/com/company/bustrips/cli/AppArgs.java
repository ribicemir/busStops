package com.company.bustrips.cli;

public record AppArgs(String stationId, int numBusesPerLine, TimeFormat timeFormat) {

    public static AppArgs parse(String[] args) {
        if (args == null || args.length != 3) {
            throw new IllegalArgumentException("Napačno število parametrov.\nUporaba: java -jar target/bus-trips-1.0.0.jar <station_id> <num_buses_per_line> <relative|absolute>");
        }

        String stationId = args[0].trim();
        if (stationId.isEmpty()) {
            throw new IllegalArgumentException("ID postaje ne sme biti prazen.");
        }

        int numBuses;
        try {
            numBuses = Integer.parseInt(args[1].trim());
            if (numBuses <= 0) {
                throw new IllegalArgumentException("Število avtobusov na linijo mora biti večje od 0 (podano: " + args[1] + ").");
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Število avtobusov mora biti veljavno celo število (podano: " + args[1] + ").");
        }

        TimeFormat format = TimeFormat.fromString(args[2]);

        return new AppArgs(stationId, numBuses, format);
    }
}