package com.company.bustrips;

import com.company.bustrips.model.Arrival;
import com.company.bustrips.parser.GtfsParser;
import com.company.bustrips.service.TimetableService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

public class TimetableServiceIntegrationTest {
    private TimetableService timetableService;
    private GtfsParser parser;

    @BeforeEach
    void setUp() {
        Path dataDir = Path.of("data");
        parser = new GtfsParser(dataDir);
        timetableService = new TimetableService(parser);
    }

    @Test
    @DisplayName("Integracijski test: uspešno branje prihodov za postajo 2 ob 12:00")
    void testGetUpcomingArrivalsForStation2() throws IOException {
        LocalTime fixedTime = LocalTime.of(12, 0);
        String stationId = "2";

        Optional<List<Arrival>> resultOpt = timetableService.getUpcomingArrivals(stationId, fixedTime);

        assertThat(resultOpt).isPresent();
        List<Arrival> arrivals = resultOpt.get();

        assertThat(arrivals).isNotEmpty();

        for (Arrival arrival : arrivals) {
            assertThat(arrival.minUntilArrival()).isBetween(0L, 120L);
            assertThat(arrival.shortNameofRoute()).isNotBlank();
        }
    }

    @Test
    @DisplayName("Integracijski test: neobstoječa postaja vrne Optional.empty")
    void testNonExistingStation() throws IOException {
        LocalTime fixedTime = LocalTime.of(12, 0);
        Optional<List<Arrival>> resultOpt = timetableService.getUpcomingArrivals("99999", fixedTime);

        assertThat(resultOpt).isEmpty();
    }

    @Test
    @DisplayName("Integracijski test: prihodi so kronološko urejeni po času")
    void testArrivalsAreSortedChronologically() throws IOException {
        LocalTime fixedTime = LocalTime.of(12, 0);
        String stationId = "2";

        Optional<List<Arrival>> resultOpt = timetableService.getUpcomingArrivals(stationId, fixedTime);

        assertThat(resultOpt).isPresent();
        List<Arrival> arrivals = resultOpt.get();
        assertThat(arrivals.size()).isGreaterThan(1);


        for (int i = 0; i < arrivals.size() - 1; i++) {
            LocalTime current = arrivals.get(i).arrivalTime();
            LocalTime next = arrivals.get(i + 1).arrivalTime();
            assertThat(current).isBeforeOrEqualTo(next);
        }
    }

    @Test
    @DisplayName("Integracijski test: postaja obstaja, vendar v 2 urah ni nobenega avtobusa")
    void testStationWithNoUpcomingArrivals() throws IOException {
        LocalTime nightTime = LocalTime.of(3, 0);
        String stationId = "2";

        Optional<List<Arrival>> resultOpt = timetableService.getUpcomingArrivals(stationId, nightTime);


        assertThat(resultOpt).isPresent();
        assertThat(resultOpt.get()).isEmpty();
    }

}
