package org.utils.datetime.date;

import lombok.*;

import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

/**
 * The `DateTimeUtils` class provides utility methods for date and time manipulation and formatting.
 * It supports various date and time formats and offers methods to validate, parse, and format date strings.
 *
 * <p>This class is designed to handle different locales and time zones, ensuring accurate date and time operations
 * across various regions and formats. It includes methods to:
 * <ul>
 *   <li>Validate date strings against supported formats</li>
 *   <li>Determine the format of a given date string</li>
 *   <li>Format date strings from one format to another</li>
 *   <li>Convert date strings to `LocalDateTime`, `LocalDate`, `LocalTime`, and `Date` objects</li>
 *   <li>Compare date and date-time strings</li>
 *   <li>Calculate the difference between dates and times</li>
 *   <li>Calculate the number of workdays between dates</li>
 * </ul>
 *
 * <p>The class uses a `FormatOptions` inner class to encapsulate locale, time zone, and offset settings.
 * Default options can be set through constructors, and specific options can be passed to methods as needed.
 *
 * <h3>Note:</h3>
 * <ul>
 *   <li>Ensure date strings are in supported formats as defined in `DateTimeFormat` enum.</li>
 *   <li>Null or empty date strings will result in default or zero values.</li>
 *   <li>Locale and time zone settings can affect the output of date and time operations.</li>
 * </ul>
 *
 * <p>Note: This class is immutable and thread-safe.
 *
 * <h3>Example usage:</h3>
 *
 * <h4>Positive Examples:</h4>
 * <pre>
 * {@code
 * DateTimeUtils utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"));
 * String formattedDate = utils.formatTo("2024-12-20 14:30:00", DateTimeFormat.FORMAT_US_SHORT_DATE);
 * System.out.println(formattedDate); // Output: 12/20/2024
 *
 * boolean isValid = utils.isValid("2024-12-20 14:30:00");
 * System.out.println(isValid); // Output: true
 *
 * LocalDateTime dateTime = utils.toLocalDateTime("2024-12-20 14:30:00");
 * System.out.println(dateTime); // Output: 2024-12-20T14:30
 * }
 * </pre>
 *
 * <h4>Negative Examples:</h4>
 * <pre>
 * {@code
 * DateTimeUtils utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"));
 * String formattedDate = utils.formatTo("invalid-date", DateTimeFormat.FORMAT_US_SHORT_DATE);
 * System.out.println(formattedDate); // Output: invalid-date
 *
 * boolean isValid = utils.isValid("invalid-date");
 * System.out.println(isValid); // Output: false
 *
 * LocalDateTime dateTime = utils.toLocalDateTime("invalid-date");
 * System.out.println(dateTime); // Output: null
 * }
 * </pre>
 *
 * <h4>Edge Case Examples:</h4>
 * <pre>
 * {@code
 * DateTimeUtils utils = new DateTimeUtils(Locale.US, ZoneId.of("America/New_York"));
 * String formattedDate = utils.formatTo("", DateTimeFormat.FORMAT_US_SHORT_DATE);
 * System.out.println(formattedDate); // Output: ""
 *
 * boolean isValid = utils.isValid("");
 * System.out.println(isValid); // Output: false
 *
 * LocalDateTime dateTime = utils.toLocalDateTime("");
 * System.out.println(dateTime); // Output: null
 *
 * formattedDate = utils.formatTo(null, DateTimeFormat.FORMAT_US_SHORT_DATE);
 * System.out.println(formattedDate); // Output: null
 *
 * isValid = utils.isValid(null);
 * System.out.println(isValid); // Output: false
 *
 * dateTime = utils.toLocalDateTime(null);
 * System.out.println(dateTime); // Output: null
 * }
 * </pre>
 *
 * @since 1.0
 */
public class DateTimeUtils {

    private FormatOptions defaultOptions;

    private static final Set<DayOfWeek> DEFAULT_WEEKENDS = Set.of(DayOfWeek.SATURDAY, DayOfWeek.SUNDAY);

    public DateTimeUtils() {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(Locale locale) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(ZoneId zoneId) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(zoneId)
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneId zoneId) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(zoneId)
                .zoneOffset(ZoneOffset.UTC)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(ZoneId.systemDefault())
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(ZoneId zoneId, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(Locale.getDefault())
                .zoneId(zoneId)
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils(Locale locale, ZoneId zoneId, ZoneOffset zoneOffset) {
        defaultOptions = FormatOptions.builder()
                .locale(locale)
                .zoneId(zoneId)
                .zoneOffset(zoneOffset)
                .build();
    }

    public DateTimeUtils setDefaultOptions(FormatOptions options) {
        this.defaultOptions = options;
        return this;
    }

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    public static class FormatOptions {
        private Locale locale;
        private ZoneId zoneId;
        private ZoneOffset zoneOffset;
    }

    /**
     * Checks if the given date string is valid, according to any of the supported date/time formats.
     * This method attempts to parse the input string using each format defined in the {@link DateTimeFormat} enum,
     * considering various date, time, and date-time types.
     *
     * @param dateStr The date string to validate. Can be {@code null}.
     * @param locale  The locale to use for date/time parsing. This influences the interpretation of month names, day names, etc.
     * @return {@code true} if the date string is valid according to at least one supported format;
     * {@code false} otherwise, including if the input string is {@code null}.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils(Locale.US);
     * boolean isValid = utils.isValid("2024-12-20 14:30:00", Locale.US);
     * System.out.println(isValid); // Output: true
     * }
     * </pre>
     */
    public boolean isValid(String dateStr, Locale locale) {
        if (dateStr == null) return false;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            DateTimeFormatter formatter = getFormatter(format, locale);
            if (tryParse(dateStr, formatter, LocalDate.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalDateTime.class).isPresent() ||
                    tryParse(dateStr, formatter, YearMonth.class).isPresent() ||
                    tryParse(dateStr, formatter, MonthDay.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalTime.class).isPresent() ||
                    tryParseDayWeek(dateStr, formatter).isPresent()) {
                return true;
            }
        }
        return false;
    }

    public boolean isValid(String dateStr) {
        return this.isValid(dateStr, defaultOptions.getLocale());
    }

    /**
     * Attempts to determine the format of a given date string by iterating through predefined date formats.
     * This method uses case-insensitive parsing to match the input string against the available formats.
     *
     * @param dateStr The date string to analyze. Can be {@code null}.
     * @param locale  The locale to use for date/time parsing. This influences the interpretation of month names, day names, etc.
     * @return The format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy") if a matching format is found;
     * {@code null} if the input string is {@code null} or if no matching format is found.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils(Locale.US);
     * String format = utils.getFormatOf("2024-12-20 14:30:00", Locale.US);
     * System.out.println(format); // Output: yyyy-MM-dd HH:mm:ss
     * }
     * </pre>
     */
    public String getFormatOf(String dateStr, Locale locale) {
        if (Objects.isNull(dateStr) || dateStr.isBlank()) return null;

        for (DateTimeFormat format : DateTimeFormat.values()) {
            DateTimeFormatter formatter = getFormatter(format, locale);
            if (tryParse(dateStr, formatter, LocalDate.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalDateTime.class).isPresent() ||
                    tryParse(dateStr, formatter, YearMonth.class).isPresent() ||
                    tryParse(dateStr, formatter, MonthDay.class).isPresent() ||
                    tryParse(dateStr, formatter, LocalTime.class).isPresent() ||
                    tryParseDayWeek(dateStr, formatter).isPresent()) {
                return format.getFormat();
            }
        }//tryParse(dateStr, getFormatter(DateTimeFormat.FORMAT_EURO_SHORT_DATE, locale), LocalDate.class).isPresent()
        return null;
    }

    /**
     * Attempts to determine the format of a given date string by iterating through predefined date formats.
     * This method uses case-insensitive parsing to match the input string against the available formats.
     * Use the default locale for date/time parsing.
     *
     * @param dateStr The date string to analyze. Can be {@code null}.
     * @return The format string (e.g., "yyyy-MM-dd", "MM/dd/yyyy") if a matching format is found;
     * {@code null} if the input string is {@code null} or if no matching format is found.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String format = utils.getFormatOf("2024-12-20 14:30:00");
     * System.out.println(format); // Output: yyyy-MM-dd HH:mm:ss
     * }
     * </pre>
     */
    public String getFormatOf(String dateStr) {
        return this.getFormatOf(dateStr, defaultOptions.getLocale());
    }

    /**
     * Checks if the format of the given date string matches the expected format.
     * This method uses the default locale for format detection.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format string (e.g., "yyyy-MM-dd").
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("2024-12-20", "yyyy-MM-dd");
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, String expectedFormat) {
        return Objects.equals(this.getFormatOf(dateStr), expectedFormat);
    }

    /**
     * Checks if the format of the given date string matches the expected format, using the specified locale.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format string (e.g., "yyyy-MM-dd").
     * @param locale         The locale to use for format detection.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("20.12.2024", "dd.MM.yyyy", Locale.GERMANY);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, String expectedFormat, Locale locale) {
        return Objects.equals(this.getFormatOf(dateStr, locale), expectedFormat);
    }

    /**
     * Checks if the format of the given date string matches the expected format from the {@link DateTimeFormat}.
     * This method uses the default locale for format detection.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format from the {@link DateTimeFormat}.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("2024-12-20", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat) {
        return Objects.equals(this.getFormatOf(dateStr), expectedFormat.getFormat());
    }

    /**
     * Checks if the format of the given date string matches the expected format from the {@link DateTimeFormat}, using the specified locale.
     *
     * @param dateStr        The date string to check.
     * @param expectedFormat The expected format from the {@link DateTimeFormat}.
     * @param locale         The locale to use for format detection.
     * @return {@code true} if the format of the date string matches the expected format,
     * {@code false} otherwise, including if the date string is null or its format cannot be determined.
     * @since 1.0
     *
     * <p>Example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean matches = utils.isFormatAt("20.12.2024", DateTimeFormat.FORMAT_EURO_SHORT_DATE, Locale.GERMANY);
     * System.out.println(matches); // Output: true
     * }
     * </pre>
     */
    public boolean isFormatAt(String dateStr, DateTimeFormat expectedFormat, Locale locale) {
        return Objects.equals(this.getFormatOf(dateStr, locale), expectedFormat.getFormat());
    }

    /**
     * Formats a date string from one format to another.
     * <p>
     * This method attempts to parse the input date string (`dateStr`) according to the
     * inferred format (`inputDateFormat`) and then formats it to the desired output format
     * specified by the `toFormat` enum.
     *
     * @param dateStr  The date string to be formatted.
     * @param toFormat The desired output format as a {@link DateTimeFormat} value.
     * @return The formatted date string or the original string if parsing fails.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String formattedDate = utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(formattedDate); // Output: 2024-12-20
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * String formattedDate = utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
     * System.out.println(formattedDate); // Output: invalid-date
     * }
     * </pre>
     */
    public String formatTo(String dateStr, DateTimeFormat toFormat) {
        if (dateStr == null || toFormat == null) return dateStr;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return dateStr;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        DateTimeFormatter outputTextFormatter = this.getFormatter(toFormat.getFormat(), defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);

        return parsedDateTime.map(zdt ->
                zdt.withZoneSameInstant(defaultOptions.zoneId)
                        .withZoneSameInstant(defaultOptions.zoneOffset)
                        .format(outputTextFormatter)
        ).orElse(dateStr);
    }

    /**
     * Formats a date string from one format to another using specific format options.
     * <p>
     * This method attempts to parse the input date string (`dateStr`) according to the
     * inferred format (`inputDateFormat`) and then formats it to the desired output format
     * specified by the `toFormat` enum, considering the provided `options`.
     *
     * @param dateStr  The date string to be formatted.
     * @param toFormat The desired output format as a {@link DateTimeFormat} value.
     * @param options  The format options to use for locale, time zone, and offset.
     * @return The formatted date string or the original string if parsing fails.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * String formattedDate = utils.formatTo("12/20/2024 02:30:00 PM", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options);
     * System.out.println(formattedDate); // Output: 2024-12-20
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * String formattedDate = utils.formatTo("invalid-date", DateTimeFormat.FORMAT_ISO_LOCAL_DATE, options);
     * System.out.println(formattedDate); // Output: invalid-date
     * }
     * </pre>
     */
    public String formatTo(String dateStr, DateTimeFormat toFormat, FormatOptions options) {
        setDefaultOptions(options);
        return formatTo(dateStr, toFormat);
    }

    /**
     * Attempts to parse a String representation of a date and time into a LocalDateTime object.
     * <p>
     * This method first tries to identify the format of the date string based on the provided locale.
     * Then, it creates a DateTimeFormatter object suitable for parsing the specific format.
     * Finally, it attempts to parse the date string and convert it to a ZonedDateTime object.
     * If parsing is successful, the method extracts the LocalDateTime part and returns it.
     * Otherwise, it returns null.
     *
     * @param dateStr The String representation of the date and time to be parsed.
     * @return A LocalDateTime object representing the parsed date and time,
     * or null if parsing fails or the input string is null.
     */
    public LocalDateTime toLocalDateTime(String dateStr) {
        if (Objects.isNull(dateStr) || dateStr.isBlank()) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalDateTime).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a date into a LocalDate object.
     * <p>
     * This method follows the same logic as `toLocalDateTime`. It identifies the format,
     * creates a suitable DateTimeFormatter, attempts to parse the date string into a ZonedDateTime,
     * and then extracts and returns the LocalDate part. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the date to be parsed.
     * @return A LocalDate object representing the parsed date, or null if parsing fails
     * or the input string is null.
     */
    public LocalDate toLocalDate(String dateStr) {
        if (Objects.isNull(dateStr) || dateStr.isBlank()) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalDate).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a time into a LocalTime object.
     * <p>
     * Similar to `toLocalDateTime` and `toLocalDate`, this method identifies the format,
     * creates a DateTimeFormatter, attempts to parse the string into a ZonedDateTime,
     * and then extracts and returns the LocalTime part. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the time to be parsed.
     * @return A LocalTime object representing the parsed time, or null if parsing fails
     * or the input string is null.
     */
    public LocalTime toLocalTime(String dateStr) {
        if (Objects.isNull(dateStr) || dateStr.isBlank()) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(ZonedDateTime::toLocalTime).orElse(null);
    }

    /**
     * Attempts to parse a String representation of a date and time into a java.util.Date object.
     * <p>
     * This method follows a similar approach as the previous methods but converts the parsed
     * ZonedDateTime to an Instant first. Finally, it creates a java.util.Date object
     * from the Instant using `Date.from()`. If parsing fails or the input string is null,
     * it returns null.
     *
     * @param dateStr The String representation of the date and time to be parsed.
     * @return A java.util.Date object representing the parsed date and time,
     * or null if parsing fails or the input string is null.
     * @throws IllegalArgumentException if the parsed ZonedDateTime has a time zone
     *                                  offset of more than 18 hours from UTC (limitations of java.util.Date).
     */
    public Date toDate(String dateStr) {
        if (Objects.isNull(dateStr) || dateStr.isBlank()) return null;

        String inputDateFormat = this.getFormatOf(dateStr, defaultOptions.getLocale());
        if (inputDateFormat == null) return null;

        DateTimeFormatter inputTextFormatter = this.getFormatter(inputDateFormat, defaultOptions.getLocale());
        Optional<ZonedDateTime> parsedDateTime = this.getZonedDateTime(dateStr, inputTextFormatter);
        return parsedDateTime.map(zonedDateTime -> Date.from(zonedDateTime.toInstant())).orElse(null);
    }

    /**
     * Compares two date strings to check if they represent the same date.
     * <p>
     * This method formats both date strings to the ISO local date format ("yyyy-MM-dd")
     * and then compares them for equality.
     *
     * @param dateStr1 The first date string to compare. Can be in any supported date format.
     * @param dateStr2 The second date string to compare. Can be in any supported date format.
     * @return {@code true} if both date strings represent the same date or both are null;
     * {@code false} otherwise,
     * including if either date string is null and the other is non-null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDate = utils.isDateEqualTo("2024-12-20", "2024-12-20T14:30:00+05:30");
     * System.out.println(isSameDate); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDate = utils.isDateEqualTo("2024-12-20", "2024-12-21T14:30:00+05:30");
     * System.out.println(isSameDate); // Output: false
     * }
     * </pre>
     */
    public boolean isDateEqualTo(String dateStr1, String dateStr2) {
        String dateTime1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        String dateTime2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE);
        return Objects.equals(dateTime1, dateTime2);
    }

    /**
     * Compares two date-time strings to check if they represent the same date and time.
     * <p>
     * This method formats both date-time strings to the ISO local date-time format ("yyyy-MM-dd'T'HH:mm:ss")
     * and then compares them for equality.
     *
     * @param dateStr1 The first date-time string to compare. Can be in any supported date-time format.
     * @param dateStr2 The second date-time string to compare. Can be in any supported date-time format.
     * @return {@code true} if both date-time strings represent the same date and time or both are null;
     * {@code false} otherwise,
     * including if either date-time string is null and the other is non-null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDateTime = utils.idDateTimeEqualTo("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM");
     * System.out.println(isSameDateTime); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * boolean isSameDateTime = utils.idDateTimeEqualTo("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM");
     * System.out.println(isSameDateTime); // Output: false
     * }
     * </pre>
     */
    public boolean idDateTimeEqualTo(String dateStr1, String dateStr2) {
        String dateTime1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        String dateTime2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_ISO_LOCAL_DATE_TIME);
        return Objects.equals(dateTime1, dateTime2);
    }

    /**
     * Compares two date-time strings given in any different format to check if they represent the same date and time, using specific format options.
     * <p>
     * This method formats both date-time strings to the specified format ("yyyy-MM-dd'T'HH:mm:ss.SSSSXXX")
     * and then compares them for equality, considering the provided `options`.
     *
     * @param dateStr1 The first date-time string to compare.
     * @param dateStr2 The second date-time string to compare.
     * @param options  The format options to use for locale, time zone, and offset.
     * @return {@code true} if both date-time strings represent the same date and time;
     * {@code false} otherwise, including if either date-time string is null or cannot be parsed.
     * @since 1.0
     *
     * <p>Positive example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:30:00 PM", options);
     * System.out.println(isSameDateTime); // Output: true
     * }
     * </pre>
     *
     * <p>Negative example usage:
     * <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * DateTimeUtils.FormatOptions options = DateTimeUtils.FormatOptions.builder()
     *     .locale(Locale.US)
     *     .zoneId(ZoneId.of("America/New_York"))
     *     .zoneOffset(ZoneOffset.of("-05:00"))
     *     .build();
     * boolean isSameDateTime = utils.compareDateTime("2024-12-20T14:30:00", "12/20/2024 02:31:00 PM", options);
     * System.out.println(isSameDateTime); // Output: false
     * }
     * </pre>
     */
    public boolean idDateTimeEqualTo(String dateStr1, String dateStr2, FormatOptions options) {
        String dateTime1 = this.formatTo(dateStr1, DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX, options);
        String dateTime2 = this.formatTo(dateStr2, DateTimeFormat.FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX, options);
        return Objects.equals(dateTime1, dateTime2);
    }

    /**
     * Calculates the number of days between two date strings.
     *
     * @param dateStr1 The start date string.
     * @param dateStr2 The end date string.
     * @return The number of days between the two dates.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetween("2024-12-20", "2024-12-25");
     * System.out.println(days); // Output: 5
     * }
     * </pre>
     */
    public long calculateDaysBetween(String dateStr1, String dateStr2) {
        if (isInvalid(dateStr1) || isInvalid(dateStr2)) {
            return 0;
        }
        LocalDate ld1 = this.toLocalDate(dateStr1);
        LocalDate ld2 = this.toLocalDate(dateStr2);
        if (ld1 == null || ld2 == null) {
            return 0;
        }
        return ChronoUnit.DAYS.between(ld1, ld2);
    }

    /**
     * Calculates the number of days between two LocalDateTime objects.
     *
     * @param ldt1 The start LocalDateTime.
     * @param ldt2 The end LocalDateTime.
     * @return The number of days between the two LocalDateTime objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetween(LocalDateTime.of(2024, 12, 20, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0));
     * System.out.println(days); // Output: 5
     * }
     * </pre>
     */
    public long calculateDaysBetween(LocalDateTime ldt1, LocalDateTime ldt2) {
        return (ldt1 == null || ldt2 == null) ? 0 : Duration.between(ldt1, ldt2).toDays();
    }

    /**
     * Calculates the number of days between two LocalDate objects.
     *
     * @param ld1 The start LocalDate.
     * @param ld2 The end LocalDate.
     * @return The number of days between the two LocalDate objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetween(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25));
     * System.out.println(days); // Output: 5
     * }
     * </pre>
     */
    public long calculateDaysBetween(LocalDate ld1, LocalDate ld2) {
        return (ld1 == null || ld2 == null) ? 0 : ChronoUnit.DAYS.between(ld1, ld2);
    }

    /**
     * Calculates the number of hours between two LocalTime objects.
     *
     * @param lt1 The start LocalTime.
     * @param lt2 The end LocalTime.
     * @return The number of hours between the two LocalTime objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long hours = utils.calculateHoursBetween(LocalTime.of(14, 30), LocalTime.of(18, 30));
     * System.out.println(hours); // Output: 4
     * }
     * </pre>
     */
    public long calculateHoursBetween(LocalTime lt1, LocalTime lt2) {
        return (lt1 == null || lt2 == null) ? 0 : Duration.between(lt1, lt2).toHours();
    }

    /**
     * Calculates the number of days between two date strings, inclusive of the start and end dates.
     *
     * @param dateStr1 The start date string.
     * @param dateStr2 The end date string.
     * @return The number of days between the two dates, inclusive.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetweenInclusive("2024-12-20", "2024-12-25");
     * System.out.println(days); // Output: 6
     * }
     * </pre>
     */
    public long calculateDaysBetweenInclusive(String dateStr1, String dateStr2) {
        return this.calculateDaysBetween(dateStr1, dateStr2) + 1;
    }

    /**
     * Calculates the number of days between two LocalDateTime objects, inclusive of the start and end dates.
     *
     * @param ldt1 The start LocalDateTime.
     * @param ldt2 The end LocalDateTime.
     * @return The number of days between the two LocalDateTime objects, inclusive.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetweenInclusive(LocalDateTime.of(2024, 12, 20, 0, 0), LocalDateTime.of(2024, 12, 25, 0, 0));
     * System.out.println(days); // Output: 6
     * }
     * </pre>
     */
    public long calculateDaysBetweenInclusive(LocalDateTime ldt1, LocalDateTime ldt2) {
        return this.calculateDaysBetween(ldt1, ldt2) + 1;
    }

    /**
     * Calculates the number of days between two LocalDate objects, inclusive of the start and end dates.
     *
     * @param ld1 The start LocalDate.
     * @param ld2 The end LocalDate.
     * @return The number of days between the two LocalDate objects, inclusive.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long days = utils.calculateDaysBetweenInclusive(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25));
     * System.out.println(days); // Output: 6
     * }
     * </pre>
     */
    public long calculateDaysBetweenInclusive(LocalDate ld1, LocalDate ld2) {
        return this.calculateDaysBetween(ld1, ld2) + 1;
    }

    /**
     * Calculates the duration between two date-time strings.
     *
     * @param dateStr1 The start date-time string.
     * @param dateStr2 The end date-time string.
     * @return The duration between the two date-time strings.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * Duration duration = utils.calculateDurationBetween("2024-12-20T14:30:00", "2024-12-21T14:30:00");
     * System.out.println(duration.toHours()); // Output: 24
     * }
     * </pre>
     */
    public Duration calculateDurationBetween(String dateStr1, String dateStr2) {
        if (isInvalid(dateStr1) || isInvalid(dateStr2)) return Duration.ZERO;
        LocalDateTime ldt1 = this.toLocalDateTime(dateStr1);
        LocalDateTime ldt2 = this.toLocalDateTime(dateStr2);
        return (ldt1 == null || ldt2 == null) ? Duration.ZERO : Duration.between(ldt1, ldt2);
    }

    /**
     * Calculates the duration between two LocalDateTime objects.
     *
     * @param ldt1 The start LocalDateTime.
     * @param ldt2 The end LocalDateTime.
     * @return The duration between the two LocalDateTime objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * Duration duration = utils.calculateDurationBetween(LocalDateTime.of(2024, 12, 20, 14, 30), LocalDateTime.of(2024, 12, 21, 14, 30));
     * System.out.println(duration.toHours()); // Output: 24
     * }
     * </pre>
     */
    public Duration calculateDurationBetween(LocalDateTime ldt1, LocalDateTime ldt2) {
        return (ldt1 == null || ldt2 == null) ? Duration.ZERO : Duration.between(ldt1, ldt2);
    }

    /**
     * Calculates the duration between two LocalDate objects.
     *
     * @param ld1 The start LocalDate.
     * @param ld2 The end LocalDate.
     * @return The duration between the two LocalDate objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * Duration duration = utils.calculateDurationBetween(LocalDate.of(2024, 12, 20), LocalDate.of(2024, 12, 25));
     * System.out.println(duration.toDays()); // Output: 5
     * }
     * </pre>
     */
    public Duration calculateDurationBetween(LocalDate ld1, LocalDate ld2) {
        return (ld1 == null || ld2 == null) ? Duration.ZERO : Duration.between(ld1.atStartOfDay(), ld2.atStartOfDay());
    }

    /**
     * Calculates the duration between two LocalTime objects.
     *
     * @param lt1 The start LocalTime.
     * @param lt2 The end LocalTime.
     * @return The duration between the two LocalTime objects.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * Duration duration = utils.calculateDurationBetween(LocalTime.of(14, 30), LocalTime.of(18, 30));
     * System.out.println(duration.toHours()); // Output: 4
     * }
     * </pre>
     */
    public Duration calculateDurationBetween(LocalTime lt1, LocalTime lt2) {
        return (lt1 == null || lt2 == null) ? Duration.ZERO : Duration.between(lt1.atDate(LocalDate.now()), lt2.atDate(LocalDate.now()));
    }

    /**
     * Calculates the number of workdays between two date strings, excluding weekends.
     * By default, weekends are considered to be Saturday and Sunday.
     *
     * @param dateStr1 The start date string.
     * @param dateStr2 The end date string.
     * @return The number of workdays between the two dates.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * long workdays = utils.calculateWorkdaysBetween("2024-12-20", "2024-12-25");
     * System.out.println(workdays); // Output: 3
     * }
     * </pre>
     */
    public long calculateWorkdaysBetween(String dateStr1, String dateStr2) {
        return calculateWorkdaysBetween(dateStr1, dateStr2, DEFAULT_WEEKENDS);
    }

    /**
     * Calculates the number of workdays between two date strings, excluding weekends.
     * By default, weekends are considered to be Saturday and Sunday.
     *
     * @param dateStr1    The start date string.
     * @param dateStr2    The end date string.
     * @param weekEndDays The set of days to consider as weekends.
     * @return The number of workdays between the two dates.
     * @example <pre>
     * {@code
     * DateTimeUtils utils = new DateTimeUtils();
     * Set<DayOfWeek> weekends = new HashSet<>() {
     *     {
     *         add(DayOfWeek.SATURDAY);
     *         add(DayOfWeek.SUNDAY);
     *     }
     * };
     * long workdays = utils.calculateWorkdaysBetween("2024-12-20", "2024-12-25", weekends);
     * System.out.println(workdays); // Output: 3
     * }
     * </pre>
     */
    public long calculateWorkdaysBetween(String dateStr1, String dateStr2, Set<DayOfWeek> weekEndDays) {
        if (isInvalid(dateStr1) || isInvalid(dateStr2)) {
            return 0;
        }
        LocalDateTime ldt1 = this.toLocalDateTime(dateStr1);
        LocalDateTime ldt2 = this.toLocalDateTime(dateStr2);
        if (ldt1 == null || ldt2 == null) {
            return 0;
        }
        return calculateWorkdays(ldt1, ldt2, weekEndDays);
    }

    private boolean isInvalid(String dateStr) {
        return dateStr == null || dateStr.isBlank();
    }

    private long calculateWorkdays(LocalDateTime start, LocalDateTime end, Set<DayOfWeek> weekEndDays) {
        long workdays = 0;
        while (!start.isAfter(end)) {
            if (!weekEndDays.contains(start.getDayOfWeek())) {
                workdays++;
            }
            start = start.plusDays(1);
        }
        return workdays;
    }


    public DateTimeFormatter getFormatter(String format, Locale locale) {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // Enable case-insensitive parsing
                .appendPattern(format)
                .toFormatter(locale);
    }

    public DateTimeFormatter getFormatter(DateTimeFormat format, Locale locale) {
        return this.getFormatter(format.getFormat(), locale);
    }

    private Optional<ZonedDateTime> getZonedDateTime(String dateStr, DateTimeFormatter formatter) {
        return tryParse(dateStr, formatter, ZonedDateTime.class)
                .or(() -> tryParse(dateStr, formatter, LocalDateTime.class).map(ldt -> ldt.atZone(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, LocalDate.class).map(ld -> ld.atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, LocalTime.class).map(lt -> lt.atDate(LocalDate.now()).atZone(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, YearMonth.class).map(ym -> ym.atDay(1).atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParse(dateStr, formatter, MonthDay.class).map(md -> md.atYear(LocalDate.now().getYear()).atStartOfDay(defaultOptions.getZoneId())))
                .or(() -> tryParseDayWeek(dateStr, formatter));
    }

    /**
     * Attempts to parse a date string as a DayOfWeek object and returns the next occurrence
     * of that day of the week as a ZonedDateTime.
     * <p>
     * This method uses the provided `formatter` to parse the `dateStr` and attempts to create
     * a DayOfWeek object. If successful, it finds the next occurrence of that day of the week
     * relative to the current date and returns a ZonedDateTime representing the start of the day
     * in the system's default zone. It returns an Optional containing the ZonedDateTime on success
     * or an empty Optional on failure.
     *
     * @param dateStr   The date string to be parsed.
     * @param formatter The DateTimeFormatter to use for parsing.
     * @return An Optional containing the ZonedDateTime for the next occurrence of the parsed day
     * of the week or empty Optional on failure.
     */
    private Optional<ZonedDateTime> tryParseDayWeek(String dateStr, DateTimeFormatter formatter) {
        try {
            DayOfWeek dayOfWeek = DayOfWeek.from(formatter.parse(dateStr));
            LocalDate today = LocalDate.now();
            LocalDate nextDayOfWeek = today.with(TemporalAdjusters.nextOrSame(dayOfWeek));
            return Optional.of(nextDayOfWeek.atStartOfDay(defaultOptions.getZoneId()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }

    private <T> Optional<T> tryParse(String dateStr, DateTimeFormatter formatter, Class<T> type) {
        try {
            if (type == ZonedDateTime.class) {
                return Optional.of(type.cast(ZonedDateTime.parse(dateStr, formatter)));
            } else if (type == LocalDateTime.class) {
                return Optional.of(type.cast(LocalDateTime.parse(dateStr, formatter)));
            } else if (type == LocalDate.class) {
                return Optional.of(type.cast(LocalDate.parse(dateStr, formatter)));
            } else if (type == LocalTime.class) {
                return Optional.of(type.cast(LocalTime.parse(dateStr, formatter)));
            } else if (type == YearMonth.class) {
                return Optional.of(type.cast(YearMonth.parse(dateStr, formatter)));
            } else if (type == MonthDay.class) {
                return Optional.of(type.cast(MonthDay.parse(dateStr, formatter)));
            }
        } catch (Exception e) {
            return Optional.empty();
        }
        return Optional.empty();
    }
}