package com.company.bustrips;

import com.company.bustrips.cli.AppArgs;
import com.company.bustrips.cli.TimeFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class AppArgsTest {

    @Test
    @DisplayName("Uspešno razčlenjevanje veljavnih argumentov")
    void testValidArguments() {
        String[] args = {"2", "3", "relative"};
        AppArgs appArgs = AppArgs.parse(args);

        assertThat(appArgs.stationId()).isEqualTo("2");
        assertThat(appArgs.numBusesPerLine()).isEqualTo(3);
        assertThat(appArgs.timeFormat()).isEqualTo(TimeFormat.RELATIVE);
    }

    @Test
    @DisplayName("Prepoznava absolutnega formata")
    void testAbsoluteFormat() {
        String[] args = {"10", "2", "absolute"};
        AppArgs appArgs = AppArgs.parse(args);

        assertThat(appArgs.timeFormat()).isEqualTo(TimeFormat.ABSOLUTE);
    }

    @Test
    @DisplayName("Napaka pri premajhnem ali prevelikem številu argumentov")
    void testInvalidNumberOfArguments() {
        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "3"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Napačno število parametrov");

        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "3", "relative", "odvečni"}))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("Napaka, če število avtobusov ni veljavno število ali je <= 0")
    void testInvalidBusNumber() {
        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "abc", "relative"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("veljavno celo število");

        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "0", "relative"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("večje od 0");

        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "-5", "relative"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("večje od 0");
    }

    @Test
    @DisplayName("Napaka pri neveljavnem formatu časa")
    void testInvalidTimeFormat() {
        assertThatThrownBy(() -> AppArgs.parse(new String[]{"2", "3", "wrong_format"}))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Neveljaven format časa");
    }
}