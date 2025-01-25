package org.utils.datetime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.utils.datetime.date.DateTimeFormat;
import org.utils.datetime.date.DateTimeUtils;
import org.utils.datetime.date.TimeZoneId;

import java.time.*;
import java.util.Locale;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    private DateTimeUtils utils;

    @BeforeEach
    void setUp() {
        utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"), ZoneOffset.of("-05:00"));
    }

    @Test
    void testIsValid() {
        assertTrue(utils.isValid("2024-12-20 14:30:00"));
        assertFalse(utils.isValid("invalid-date"));
    }

    @Test
    void testIsValidWithLocale() {
        assertTrue(utils.isValid("20.12.2024", Locale.GERMANY));
        assertFalse(utils.isValid("invalid-date", Locale.GERMANY));
    }

    @Test
    void testGetFormatOf() {
        assertEquals("yyyy-MM-dd HH:mm:ss", utils.getFormatOf("2024-12-20 14:30:00"));
        assertNull(utils.getFormatOf("invalid-date"));
    }

    @Test
    void testGetFormatOfWithLocale() {
        assertEquals("dd.MM.yyyy", utils.getFormatOf("20.12.2024", Locale.GERMANY));
        assertNull(utils.getFormatOf("invalid-date", Locale.GERMANY));
    }

    @Test
    void testIsFormatAt() {
        assertTrue(utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertFalse(utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
    }

    @Test
    void testIsFormatAtWithLocale() {
        assertTrue(utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY));
        assertFalse(utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY));
    }

    @Test
    void testFormatTo() {
        assertEquals("2024-12-20", utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertEquals("2024-12-20", utils.formatTo("20-12-2024", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
        assertEquals("invalid-date", utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE));
    }

    @Test
    void testFormatToWithOptions() {
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("America/New_York"))
                .zoneOffset(ZoneOffset.of("-05:00"))
                .build();
        assertEquals("2024-12-20", utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options));
        assertEquals("invalid-date", utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options));
    }

    @Test
    void testIsDateEqualTo() {
        // Positive cases
        assertTrue(utils.isDateEqualTo("2024-12-20", "2024-12-20"));
        assertTrue(utils.isDateEqualTo("20.12.2024", "20.12.2024"));

        // Negative cases
        assertFalse(utils.isDateEqualTo("2024-12-20", "2024-12-21"));
        assertFalse(utils.isDateEqualTo("20.12.2024", "21.12.2024"));

        // Edge cases
        assertFalse(utils.isDateEqualTo("", "2024-12-20"));
        assertFalse(utils.isDateEqualTo("2024-12-20", ""));
        assertFalse(utils.isDateEqualTo(null, "2024-12-20"));
        assertFalse(utils.isDateEqualTo("2024-12-20", null));
    }

    @Test
    void testIsDateEqualToTime() {
        // Positive cases
        assertTrue(utils.idDateTimeEqualTo("2024-12-20T14:30:00", "2024-12-20T14:30:00"));
        assertTrue(utils.idDateTimeEqualTo("20.12.2024 14:30:00", "20.12.2024 14:30:00"));

        // Negative cases
        assertFalse(utils.idDateTimeEqualTo("2024-12-20T14:30:00", "2024-12-20T15:30:00"));
        assertFalse(utils.idDateTimeEqualTo("20.12.2024 14:30:00", "21.12.2024 14:30:00"));

        // Edge cases
        assertFalse(utils.idDateTimeEqualTo("", "2024-12-20T14:30:00"));
        assertFalse(utils.idDateTimeEqualTo("2024-12-20T14:30:00", ""));
        assertFalse(utils.idDateTimeEqualTo(null, "2024-12-20T14:30:00"));
        assertFalse(utils.idDateTimeEqualTo("2024-12-20T14:30:00", null));
    }

    @Test
    void testIsDateEqualToTimeWithOptions() {
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("America/New_York"))
                .zoneOffset(ZoneOffset.of("-05:00"))
                .build();

        // Positive cases
        assertTrue(utils.idDateTimeEqualTo("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM", options));
        assertTrue(utils.idDateTimeEqualTo("20.12.2024 14:30:00", "12/20/2024 02:30:00 PM", options));

        // Negative cases
        assertFalse(utils.idDateTimeEqualTo("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM", options));
        assertFalse(utils.idDateTimeEqualTo("20.12.2024 14:30:00", "12/21/2024 02:30:00 PM", options));

        // Edge cases
        assertFalse(utils.idDateTimeEqualTo("", "12/20/2024 02:30:00 PM", options));
        assertFalse(utils.idDateTimeEqualTo("12/20/2024 02:30:00 PM", "", options));
        assertFalse(utils.idDateTimeEqualTo(null, "12/20/2024 02:30:00 PM", options));
        assertFalse(utils.idDateTimeEqualTo("12/20/2024 02:30:00 PM", null, options));
    }

    @Test
    void calculateDaysBetween_validDates() {
        assertEquals(5, utils.calculateDaysBetween("2024-12-20", "2024-12-25"));
    }

    @Test
    void calculateDaysBetween_invalidDates() {
        assertEquals(0, utils.calculateDaysBetween("2024-13-20", "2024-12-25"));
        assertEquals(0, utils.calculateDaysBetween("2024-12-20", "2024-12-32"));
    }

    @Test
    void calculateDaysBetween_nullDates() {
        assertEquals(0, utils.calculateDaysBetween(null, "2024-12-25"));
        assertEquals(0, utils.calculateDaysBetween("2024-12-20", null));
    }

    @Test
    void calculateDaysBetween_emptyDates() {
        assertEquals(0, utils.calculateDaysBetween("", "2024-12-25"));
        assertEquals(0, utils.calculateDaysBetween("2024-12-20", ""));
    }

    @Test
    void calculateDaysBetweenLocalDateTime_validDates() {
        assertEquals(5, utils.calculateDaysBetween(LocalDateTime.of(2024, 12, 20, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0)));
    }

    @Test
    void calculateDaysBetweenLocalDateTime_nullDates() {
        assertEquals(0, utils.calculateDaysBetween((LocalDateTime) null, LocalDateTime.of(2024, 12, 25, 0, 0)));
        assertEquals(0, utils.calculateDaysBetween(LocalDateTime.of(2024, 12, 20, 0, 0), null));
    }

    @Test
    void calculateDaysBetweenLocalDate_validDates() {
        assertEquals(5, utils.calculateDaysBetween(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25)));
    }

    @Test
    void calculateDaysBetweenLocalDate_nullDates() {
        assertEquals(0, utils.calculateDaysBetween((LocalDate) null, LocalDate.of(2024, 12, 25)));
        assertEquals(0, utils.calculateDaysBetween(LocalDate.of(2024, 12, 20), null));
    }

    @Test
    void calculateHoursBetween_validTimes() {
        assertEquals(4, utils.calculateHoursBetween(LocalTime.of(14, 30), LocalTime.of(18, 30)));
    }

    @Test
    void calculateHoursBetween_nullTimes() {
        assertEquals(0, utils.calculateHoursBetween(null, LocalTime.of(18, 30)));
        assertEquals(0, utils.calculateHoursBetween(LocalTime.of(14, 30), null));
    }

    @Test
    void calculateDaysBetweenInclusive_validDates() {
        assertEquals(6, utils.calculateDaysBetweenInclusive("2024-12-20", "2024-12-25"));
    }

    @Test
    void calculateDaysBetweenInclusive_invalidDates() {
        assertEquals(1, utils.calculateDaysBetweenInclusive("2024-13-20", "2024-12-25"));
        assertEquals(1, utils.calculateDaysBetweenInclusive("2024-12-20", "2024-12-32"));
    }

    @Test
    void calculateDaysBetweenInclusive_nullDates() {
        assertEquals(1, utils.calculateDaysBetweenInclusive(null, "2024-12-25"));
        assertEquals(1, utils.calculateDaysBetweenInclusive("2024-12-20", null));
    }

    @Test
    void calculateDaysBetweenInclusive_emptyDates() {
        assertEquals(1, utils.calculateDaysBetweenInclusive("", "2024-12-25"));
        assertEquals(1, utils.calculateDaysBetweenInclusive("2024-12-20", ""));
    }

    @Test
    void calculateDaysBetweenInclusiveLocalDateTime_validDates() {
        assertEquals(6, utils.calculateDaysBetweenInclusive(LocalDateTime.of(2024, 12, 20, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0)));
    }

    @Test
    void calculateDaysBetweenInclusiveLocalDateTime_nullDates() {
        assertEquals(1, utils.calculateDaysBetweenInclusive((LocalDateTime) null, LocalDateTime.of(2024, 12, 25, 0, 0)));
        assertEquals(1, utils.calculateDaysBetweenInclusive(LocalDateTime.of(2024, 12, 20, 0, 0), null));
    }

    @Test
    void calculateDaysBetweenInclusiveLocalDate_validDates() {
        assertEquals(6, utils.calculateDaysBetweenInclusive(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25)));
    }

    @Test
    void calculateDaysBetweenInclusiveLocalDate_nullDates() {
        assertEquals(1, utils.calculateDaysBetweenInclusive((LocalDate) null, LocalDate.of(2024, 12, 25)));
        assertEquals(1, utils.calculateDaysBetweenInclusive(LocalDate.of(2024, 12, 20), null));
    }

    @Test
    void calculateDurationBetween_validDateTimes() {
        assertEquals(Duration.ofHours(24), utils.calculateDurationBetween("2024-12-20T14:30:00", "2024-12-21T14:30:00"));
    }

    @Test
    void calculateDurationBetween_invalidDateTimes() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween("2024-13-20T14:30:00", "2024-12-21T14:30:00"));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween("2024-12-20T14:30:00", "2024-12-32T14:30:00"));
    }

    @Test
    void calculateDurationBetween_nullDateTimes() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween(null, "2024-12-21T14:30:00"));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween("2024-12-20T14:30:00", null));
    }

    @Test
    void calculateDurationBetween_emptyDateTimes() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween("", "2024-12-21T14:30:00"));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween("2024-12-20T14:30:00", ""));
    }

    @Test
    void calculateDurationBetweenLocalDateTime_validDateTimes() {
        assertEquals(Duration.ofHours(24), utils.calculateDurationBetween(LocalDateTime.of(2024, 12, 20, 14, 30), LocalDateTime.of(2024, 12, 21, 14, 30)));
    }

    @Test
    void calculateDurationBetweenLocalDateTime_nullDateTimes() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween((LocalDateTime) null, LocalDateTime.of(2024, 12, 21, 14, 30)));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween(LocalDateTime.of(2024, 12, 20, 14, 30), null));
    }

    @Test
    void calculateDurationBetweenLocalDate_validDates() {
        assertEquals(Duration.ofDays(5), utils.calculateDurationBetween(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25)));
    }

    @Test
    void calculateDurationBetweenLocalDate_nullDates() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween((LocalDate) null, LocalDate.of(2024, 12, 25)));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween(LocalDate.of(2024, 12, 20), null));
    }

    @Test
    void calculateDurationBetweenLocalTime_validTimes() {
        assertEquals(Duration.ofHours(4), utils.calculateDurationBetween(LocalTime.of(14, 30), LocalTime.of(18, 30)));
    }

    @Test
    void calculateDurationBetweenLocalTime_nullTimes() {
        assertEquals(Duration.ZERO, utils.calculateDurationBetween(null, LocalTime.of(18, 30)));
        assertEquals(Duration.ZERO, utils.calculateDurationBetween(LocalTime.of(14, 30), null));
    }

    @Test
    void calculateWorkdaysBetween_validDates() {
        assertEquals(4, utils.calculateWorkdaysBetween("2024-12-20", "2024-12-25"));
    }

    @Test
    void calculateWorkdaysBetween_invalidDates() {
        assertEquals(0, utils.calculateWorkdaysBetween("2024-13-20", "2024-12-25"));
        assertEquals(0, utils.calculateWorkdaysBetween("2024-12-20", "2024-12-32"));
    }

    @Test
    void calculateWorkdaysBetween_nullDates() {
        assertEquals(0, utils.calculateWorkdaysBetween(null, "2024-12-25"));
        assertEquals(0, utils.calculateWorkdaysBetween("2024-12-20", null));
    }

    @Test
    void calculateWorkdaysBetween_emptyDates() {
        assertEquals(0, utils.calculateWorkdaysBetween("", "2024-12-25"));
        assertEquals(0, utils.calculateWorkdaysBetween("2024-12-20", ""));
    }

    @Test
    void calculateWorkdaysBetween_withCustomWeekends() {
        Set<DayOfWeek> customWeekends = Set.of(DayOfWeek.FRIDAY, DayOfWeek.SATURDAY);
        assertEquals(4, utils.calculateWorkdaysBetween("2024-12-20", "2024-12-25", customWeekends));
    }

    @Test
    public void testEqual() {
        DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
                .locale(Locale.US)
                .zoneId(ZoneId.of("UTC")) // Use the desired time zone
                .zoneOffset(ZoneOffset.UTC) // Use the desired offset
                .build();
        DateTimeUtils utils = new DateTimeUtils(options);
        assertTrue(utils.isDateEqualTo("1992-05-03T12:00:00", "03-05-1992"));
        String date1 = utils.formatTo("2000-08-04T12:00:00", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        String date2 = utils.formatTo("04-08-2000", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        System.out.println(date1);
        System.out.println(date2);
        assertTrue(utils.isDateEqualTo("2024-12-20", "2024-12-20T14:30:00+05:30"));
    }
}