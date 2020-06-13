package com.datastax.sample;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.datastax.sample.entity.TimeserieDailyKey;

public class TestMyDates {
    
    @Test
    @DisplayName("Date format should work")
    public void should_format_yyyymmdd() {
        Assertions.assertEquals("20200601", 
                TimeserieDailyKey.YYYYMMDD.format(LocalDateTime.of(2020, 06, 01, 00, 00)));
    }

}
