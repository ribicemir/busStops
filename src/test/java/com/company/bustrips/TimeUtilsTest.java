package com.company.bustrips;
import com.company.bustrips.cli.TimeFormat;
import com.company.bustrips.util.TimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;

public class TimeUtilsTest {
    @Test
    @DisplayName("Pretvorba gtfs v localtime")
    void testParserGtfsToNormal(){
        LocalTime parsed= TimeUtils.parseGivenTime("14:30:15");
        assertThat(parsed).isEqualTo(LocalTime.of(14,30,15));
    }

    @Test
    @DisplayName("Pretvorba v primeru ura>24")
    void testParserOver24H(){
        assertThat(TimeUtils.parseGivenTime("24:00:00")).isEqualTo(LocalTime.of(0, 0, 0));
        assertThat(TimeUtils.parseGivenTime("25:15:30")).isEqualTo(LocalTime.of(1,15,30));
    }

    @Test
    @DisplayName("Izračun minut med dvema časoma znotraj istega dneva")
    void testGetMinutesBetweenSameDay() {
        LocalTime now = LocalTime.of(12, 0);
        LocalTime arrival = LocalTime.of(12, 45);

        long minutes = TimeUtils.getMinutesInBetween(now, arrival);
        assertThat(minutes).isEqualTo(45);
    }

    @Test
    @DisplayName("Izračun minut ob prehodu čez polnoč (npr. 23:50 -> 00:15)")
    void testGetMinutesBetweenMidnightCrossover() {
        LocalTime now = LocalTime.of(23, 50);
        LocalTime arrival = LocalTime.of(0, 15);


        long minutes = TimeUtils.getMinutesInBetween(now, arrival);
        assertThat(minutes).isEqualTo(25);
    }

    @Test
    @DisplayName("Preverjanje 2-urnega intervala vključno z mejnimi vrednostmi")
    void testIsWithinTwoHours() {
        LocalTime now = LocalTime.of(12, 0);

        assertThat(TimeUtils.isWithin2Hours(now, LocalTime.of(12, 0))).isTrue();
        assertThat(TimeUtils.isWithin2Hours(now, LocalTime.of(12, 45))).isTrue();
        assertThat(TimeUtils.isWithin2Hours(now, LocalTime.of(14, 0))).isTrue();
        assertThat(TimeUtils.isWithin2Hours(now, LocalTime.of(14, 1))).isFalse();
    }
    @Test
    @DisplayName("Formatiranje v relativno (min) in absolutno (HH:mm) obliko")
    void testFormatTime() {
        LocalTime now = LocalTime.of(12, 0);
        LocalTime arrival = LocalTime.of(12, 15);

        assertThat(TimeUtils.formatTime(now, arrival, TimeFormat.RELATIVE)).isEqualTo("15min");
        assertThat(TimeUtils.formatTime(now, arrival, TimeFormat.RELATIVE)).isEqualTo("15min");


        assertThat(TimeUtils.formatTime(now, arrival, TimeFormat.ABSOLUTE)).isEqualTo("12:15");
    }
}
