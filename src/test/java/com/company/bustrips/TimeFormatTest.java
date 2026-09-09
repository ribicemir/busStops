package com.company.bustrips;

import com.company.bustrips.cli.TimeFormat;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class TimeFormatTest {

    @Test
    @DisplayName("Pravilna pretvorba veljavnih nizov ne glede na velike/male črke")
    void testFromStringValid() {
        assertThat(TimeFormat.fromString("relative")).isEqualTo(TimeFormat.RELATIVE);
        assertThat(TimeFormat.fromString("RELATIVE")).isEqualTo(TimeFormat.RELATIVE);
        assertThat(TimeFormat.fromString("absolute")).isEqualTo(TimeFormat.ABSOLUTE);
        assertThat(TimeFormat.fromString("Absolute")).isEqualTo(TimeFormat.ABSOLUTE);
    }

    @Test
    @DisplayName("Napaka ob neveljavnem nizu ali null vrednosti")
    void testFromStringInvalid() {
        assertThatThrownBy(() -> TimeFormat.fromString("drugo"))
                .isInstanceOf(IllegalArgumentException.class);

        assertThatThrownBy(() -> TimeFormat.fromString(null))
                .isInstanceOf(IllegalArgumentException.class);
    }
}