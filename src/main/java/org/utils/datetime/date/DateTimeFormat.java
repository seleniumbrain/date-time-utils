package org.utils.datetime.date;

import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public enum DateTimeFormat {

    // ISO 8601 Formats
    FORMAT_ISO_LOCAL_DATE("yyyy-MM-dd"), // Example: 2024-12-20
    FORMAT_ISO_LOCAL_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss"), // Example: 2024-12-20T14:30:00
    FORMAT_ISO_OFFSET_DATE_TIME("yyyy-MM-dd'T'HH:mm:ssXXX"), // Example: 2024-12-20T14:30:00+05:30
    FORMAT_ISO_INSTANT("yyyy-MM-dd'T'HH:mm:ss'Z'"), // Example: 2024-12-20T14:30:00Z
    FORMAT_ISO_DATE("yyyyMMdd"), // Example: 20241220
    FORMAT_ISO_ORDINAL_DATE("yyyy-DDD"), // Example: 2024-355
    FORMAT_ISO_WEEK_DATE("yyyy-Www-D"), // Example: 2024-W51-5

    // US Formats
    FORMAT_US_SHORT_DATE("MM/dd/yyyy"), // Example: 12/20/2024
    FORMAT_US_MEDIUM_DATE("MMM dd, yyyy"), // Example: Dec 20, 2024
    FORMAT_US_LONG_DATE("MMMM d, yyyy"), // Example: December 20, 2024
    FORMAT_US_FULL_DATE("EEEE, MMMM d, yyyy"), // Example: Friday, December 20, 2024
    FORMAT_US_SHORT_DATE_TIME("MM/dd/yyyy HH:mm:ss"), // Example: 12/20/2024 14:30:00
    FORMAT_US_SHORT_DATE_TIME_AM_PM("MM/dd/yyyy hh:mm:ss a"), // Example: 12/20/2024 02:30:00 PM
    FORMAT_US_MEDIUM_DATE_TIME("MMM dd, yyyy HH:mm:ss"), // Example: Dec 20, 2024 14:30:00
    FORMAT_US_MEDIUM_DATE_TIME_am_pm("MMM dd, yyyy hh:mm:ss a"), // Example: Dec 20, 2024 02:30:00 PM
    FORMAT_US_MONTH_YEAR("MMMM yyyy"), // Example: December 2024
    FORMAT_US_MONTH_DAY("MMMM dd"), // Example: December 20

    // European Formats
    FORMAT_EURO_SHORT_DATE("dd.MM.yyyy"), // Example: 20.12.2024
    FORMAT_EURO_MEDIUM_DATE("dd.MMM.yyyy"), // Example: 20.Dec.2024
    FORMAT_EURO_LONG_DATE("dd. MMMM yyyy"), // Example: 20. December 2024
    FORMAT_EURO_FULL_DATE("EEEE, dd. MMMM yyyy"), // Example: Friday, 20. December 2024
    FORMAT_EURO_SHORT_DATE_TIME("dd.MM.yyyy HH:mm:ss"), // Example: 20.12.2024 14:30:00
    FORMAT_EURO_SHORT_DATE_TIME_24("dd.MM.yyyy HH:mm"), // Example: 20.12.2024 14:30
    FORMAT_EURO_DAY_MONTH("dd. MMMM"), // Example: 20. December
    FORMAT_EURO_YEAR_MONTH("MMMM yyyy"), // Example: December 2024
    FORMAT_EURO_YYYYMMDD("yyyyMMdd"), // Example: 20241220

    // British Formats
    FORMAT_BRITISH_SHORT_DATE("dd/MM/yyyy"), // Example: 20/12/2024
    FORMAT_BRITISH_MEDIUM_DATE("dd MMM yyyy"), // Example: 20 Dec 2024
    FORMAT_BRITISH_LONG_DATE("dd MMMM yyyy"), // Example: 20 December 2024
    FORMAT_BRITISH_FULL_DATE("EEEE dd MMMM yyyy"), // Example: Friday 20 December 2024

    // Japanese Formats
    FORMAT_JAPANESE_YYYY_MM_DD("yyyy/MM/dd"), // Example: 2024/12/20
    FORMAT_JAPANESE_YYYYMMDD("yyyyMMdd"), // Example: 20241220
    FORMAT_JAPANESE_YYMMDD("yyMMdd"), // Example: 241220

    // Time Formats
    FORMAT_TIME_HH_MM_SS("HH:mm:ss"), // Example: 14:30:00
    FORMAT_TIME_hh_MM_SS("hh:mm:ss"), // Example: 11:30:00
    FORMAT_TIME_HH_MM("HH:mm"), // Example: 14:30
    FORMAT_TIME_hh_MM("hh:mm"), // Example: 11:30
    FORMAT_TIME_HHMMSS("HHmmss"), // Example: 143000
    FORMAT_TIME_hhMMSS("hhmmss"), // Example: 113000
    FORMAT_TIME_HHMM("HHmm"), // Example: 1430
    FORMAT_TIME_hhMM("hhmm"), // Example: 1130
    FORMAT_TIME_AM_PM_HH_MM_SS("HH:mm:ss a"), // Example: 02:30:00 PM
    FORMAT_TIME_AM_PM_hh_MM_SS("hh:mm:ss a"), // Example: 11:30:00 AM
    FORMAT_TIME_AM_PM_HH_MM("HH:mm a"), // Example: 02:30 PM
    FORMAT_TIME_AM_PM_hh_MM("hh:mm a"), // Example: 02:30 AM
    FORMAT_TIME_HH_MM_SS_SSS("HH:mm:ss.SSS"), // Example: 14:30:00.123
    FORMAT_TIME_hh_MM_SS_SSS("hh:mm:ss.SSS"), // Example: 11:30:00.123
    FORMAT_TIME_HH_MM_SS_SSSSSSSSS("HH:mm:ss.SSSSSSSSS"), // Example: 14:30:00.123456789
    FORMAT_TIME_hh_MM_SS_SSSSSSSSS("hh:mm:ss.SSSSSSSSS"), // Example: 11:30:00.123456789
    FORMAT_TIME_AM_PM_HH_MM_SS_WITH_MILLIS("HH:mm:ss.SSS a"), // Example: 02:30:00.123 PM
    FORMAT_TIME_AM_PM_hh_MM_SS_WITH_MILLIS("hh:mm:ss.SSS a"), // Example: 02:30:00.123 AM

    // Other Common Formats
    FORMAT_COMMON_YYYYMMDD_HHMMSS("yyyyMMddHHmmss"), // Example: 20241220143000
    FORMAT_COMMON_YYYYMMDD_HHMM("yyyyMMddHHmm"), // Example: 202412201430
    FORMAT_COMMON_YYYY_MM("yyyy-MM"), // Example: 2024-12
    FORMAT_COMMON_MM_YYYY("MM-yyyy"), // Example: 12-2024
    FORMAT_COMMON_MMM_YYYY("MMM yyyy"), // Example: Dec 2024
    FORMAT_COMMON_MMMM_YYYY("MMMM yyyy"), // Example: December 2024
    FORMAT_COMMON_MMM_DD("MMM dd"), // Example: Dec 20
    FORMAT_COMMON_MMMM_DD("MMMM dd"), // Example: December 20
    FORMAT_COMMON_D_MMM_YYYY("d MMM yyyy"), // Example: 20 Dec 2024
    FORMAT_COMMON_D_MMMM_YYYY("d MMMM yyyy"), // Example: 20 December 2024
    FORMAT_COMMON_YYYY_DOT_MM_DOT_DD("yyyy.MM.dd"), // Example: 2024.12.20
    FORMAT_COMMON_DD_DOT_MM_DOT_YYYY("dd.MM.yyyy"), // Example: 20.12.2024
    FORMAT_COMMON_YYYY_MMM("yyyy MMM"), // Example: 2024 Dec
    FORMAT_COMMON_YYYY_MMMM("yyyy MMMM"), // Example: 2024 December
    FORMAT_YYYY_MMM_DD("yyyy-MMM-dd"), // Example: 2024-Dec-20
    FORMAT_YYYY_MMMM_DD("yyyy-MMMM-dd"), // Example: 2024-December-20
    FORMAT_YYYY_MM_DD_HH_MM_SS("yyyy-MM-dd HH:mm:ss"), // Example: 2024-12-20 14:30:00
    FORMAT_YYYY_MM_DD__HH_MM_SS("yyyy-MM-dd - HH:mm:ss"), // Example: 2024-12-20 - 14:30:00
    FORMAT_YYYY_MM_DD_T_HH_MM("yyyy-MM-dd'T'HH:mm"), // Example: 2024-12-20 14:30:00
    FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS("yyyy-MM-dd'T'HH:mm:ss.SSS"), // Example: 2024-12-20T14:30:00.123
    FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSS_X("yyyy-MM-dd'T'HH:mm:ss.SSSX"), // Example: 2024-12-20T14:30:00.123+05:30
    FORMAT_YYYY_MM_DD_HH_MM_SS_SSS_X("yyyy-MM-dd HH:mm:ss.SSSX"), // Example: 2024-12-20 14:30:00.123+05:30
    FORMAT_YYYY_MM_DD_T_HH_MM_SS_SSSS_XXX("yyyy-MM-dd'T'HH:mm:ss.SSSSXXX"), // Example: 2024-12-20T14:30:00.1234+05:30
    FORMAT_YYYY_MM_DD_HH_MM_SS_SSSS_XXX("yyyy-MM-dd HH:mm:ss.SSSSXXX"), // Example: 2024-12-20 14:30:00.1234+05:30
    FORMAT_DD_MM_YYYY("dd-MM-yyyy"), // Example: 20-12-2024
    FORMAT_DD_MMM_YYYY("dd-MMM-yyyy"), // Example: 20-Dec-2024
    FORMAT_DD_MMM_YYYY_HH_MM_SS("dd-MMM-yyyy - HH:mm:ss"), // Example: 20-Dec-2024 - 14:30:00
    FORMAT_DD_MMMM_YYYY("dd-MMMM-yyyy"), // Example: 20-December-2024
    FORMAT_DD_MMM_COMMA_YYYY("dd MMM, yyyy"), // Example: 20 Dec, 2024
    FORMAT_DD_MON_COMMA_YYYY("dd MMMM, yyyy"), // Example: 20 December, 2024
    FORMAT_MMM_DD_COMMA_YYYY("MMM dd, yyyy"), // Example: Dec 20, 2024
    FORMAT_MMMM_DD_COMMA_YYYY("MMMM dd, yyyy"), // Example: December 20, 2024
    FORMAT_DAY_OF_WEEK_SHORT("EEE"), // Example: Fri
    FORMAT_DAY_OF_WEEK_FULL("EEEE"), // Example: Friday
    FORMAT_RFC_1123_DATE_TIME("EEE, dd MMM yyyy HH:mm:ss Z"); // Example: Fri, 20 Dec 2024 14:30:00 +0530

    private final String format;

    DateTimeFormat(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }

    public DateTimeFormatter getFormatter() {
        return new DateTimeFormatterBuilder()
                .parseCaseInsensitive()  // Enable case-insensitive parsing
                .appendPattern(format)
                .toFormatter(Locale.US);
    }

    private static final Map<String, DateTimeFormat> ENUM_MAP;

    static {
        Map<String, DateTimeFormat> map = new ConcurrentHashMap<>();
        for (DateTimeFormat format : DateTimeFormat.values()) {
            map.put(format.getFormat().toLowerCase(), format);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    public static Optional<DateTimeFormat> get(String formatCode) {
        try {
            return Optional.of(ENUM_MAP.get(formatCode.toLowerCase()));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}
