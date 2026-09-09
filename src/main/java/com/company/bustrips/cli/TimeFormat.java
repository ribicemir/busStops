package com.company.bustrips.cli;

public enum TimeFormat {
    RELATIVE,
    ABSOLUTE;

    public static TimeFormat fromString(String value) {
        if (value == null) {
            throw new IllegalArgumentException("Format časa ne sme biti prazen. Dovoljeno: relative | absolute");
        }
        return switch (value.trim().toLowerCase()) {
            case "relative" -> RELATIVE;
            case "absolute" -> ABSOLUTE;
            default -> throw new IllegalArgumentException("Neveljaven format časa: '" + value + "'. Dovoljeno: relative | absolute");
        };
    }
}