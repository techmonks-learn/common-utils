package com.tm.common.dateutil;

import org.apache.commons.lang3.StringUtils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class TitanDateUtils {

    private static final List<TimeZoneMapping> ZONEMAPPINGS = new ArrayList<TimeZoneMapping>();

    static {
        ZONEMAPPINGS.add(new TimeZoneMapping("Afghanistan Standard Time", "Asia/Kabul", "(GMT +04:30) Kabul"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Alaskan Standard Time", "America/Anchorage", "(GMT -09:00) Alaska"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Arab Standard Time", "Asia/Riyadh", "(GMT +03:00) Kuwait, Riyadh"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Arabian Standard Time", "Asia/Dubai", "(GMT +04:00) Abu Dhabi, Muscat"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Arabic Standard Time", "Asia/Baghdad", "(GMT +03:00) Baghdad"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Argentina Standard Time", "America/Buenos_Aires", "(GMT -03:00) Buenos Aires"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Atlantic Standard Time", "America/Halifax", "(GMT -04:00) Atlantic Time (Canada)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("AUS Central Standard Time", "Australia/Darwin", "(GMT +09:30) Darwin"));
        ZONEMAPPINGS.add(new TimeZoneMapping("AUS Eastern Standard Time", "Australia/Sydney", "(GMT +10:00) Canberra, Melbourne, Sydney"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Azerbaijan Standard Time", "Asia/Baku", "(GMT +04:00) Baku"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Azores Standard Time", "Atlantic/Azores", "(GMT -01:00) Azores"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Bangladesh Standard Time", "Asia/Dhaka", "(GMT +06:00) Dhaka"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Canada Central Standard Time", "America/Regina", "(GMT -06:00) Saskatchewan"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Cape Verde Standard Time", "Atlantic/Cape_Verde", "(GMT -01:00) Cape Verde Is."));
        ZONEMAPPINGS.add(new TimeZoneMapping("Caucasus Standard Time", "Asia/Yerevan", "(GMT +04:00) Yerevan"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Cen. Australia Standard Time", "Australia/Adelaide", "(GMT +09:30) Adelaide"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central America Standard Time", "America/Guatemala", "(GMT -06:00) Central America"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Asia Standard Time", "Asia/Almaty", "(GMT +06:00) Astana"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Brazilian Standard Time", "America/Cuiaba", "(GMT -04:00) Cuiaba"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Europe Standard Time", "Europe/Budapest", "(GMT +01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central European Standard Time", "Europe/Warsaw", "(GMT +01:00) Sarajevo, Skopje, Warsaw, Zagreb"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Pacific Standard Time", "Pacific/Guadalcanal", "(GMT +11:00) Solomon Is., New Caledonia"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Standard Time (Mexico)", "America/Mexico_City", "(GMT -06:00) Guadalajara, Mexico City, Monterrey"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Central Standard Time", "America/Chicago", "(GMT -06:00) Central Time (US & Canada)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("China Standard Time", "Asia/Shanghai", "(GMT +08:00) Beijing, Chongqing, Hong Kong, Urumqi"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Dateline Standard Time", "Etc/GMT+12", "(GMT -12:00) International Date Line West"));
        ZONEMAPPINGS.add(new TimeZoneMapping("E. Africa Standard Time", "Africa/Nairobi", "(GMT +03:00) Nairobi"));
        ZONEMAPPINGS.add(new TimeZoneMapping("E. Australia Standard Time", "Australia/Brisbane", "(GMT +10:00) Brisbane"));
        ZONEMAPPINGS.add(new TimeZoneMapping("E. Europe Standard Time", "Europe/Minsk", "(GMT +02:00) Minsk"));
        ZONEMAPPINGS.add(new TimeZoneMapping("E. South America Standard Time", "America/Sao_Paulo", "(GMT -03:00) Brasilia"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Eastern Standard Time", "America/New_York", "(GMT -05:00) Eastern Time (US & Canada)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Egypt Standard Time", "Africa/Cairo", "(GMT +02:00) Cairo"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Ekaterinburg Standard Time", "Asia/Yekaterinburg", "(GMT +05:00) Ekaterinburg"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Fiji Standard Time", "Pacific/Fiji", "(GMT +12:00) Fiji, Marshall Is."));
        ZONEMAPPINGS.add(new TimeZoneMapping("FLE Standard Time", "Europe/Kiev", "(GMT +02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Georgian Standard Time", "Asia/Tbilisi", "(GMT +04:00) Tbilisi"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GMT Standard Time", "Europe/London", "(GMT) Dublin, Edinburgh, Lisbon, London"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Greenland Standard Time", "America/Godthab", "(GMT -03:00) Greenland"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Greenwich Standard Time", "Atlantic/Reykjavik", "(GMT) Monrovia, Reykjavik"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GTB Standard Time", "Europe/Istanbul", "(GMT +02:00) Athens, Bucharest, Istanbul"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Hawaiian Standard Time", "Pacific/Honolulu", "(GMT -10:00) Hawaii"));
        ZONEMAPPINGS.add(new TimeZoneMapping("India Standard Time", "Asia/Calcutta", "(GMT +05:30) Chennai, Kolkata, Mumbai, New Delhi"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Iran Standard Time", "Asia/Tehran", "(GMT +03:30) Tehran"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Israel Standard Time", "Asia/Jerusalem", "(GMT +02:00) Jerusalem"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Jordan Standard Time", "Asia/Amman", "(GMT +02:00) Amman"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Kamchatka Standard Time", "Asia/Kamchatka", "(GMT +12:00) Petropavlovsk-Kamchatsky - Old"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Korea Standard Time", "Asia/Seoul", "(GMT +09:00) Seoul"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Magadan Standard Time", "Asia/Magadan", "(GMT +11:00) Magadan"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Mauritius Standard Time", "Indian/Mauritius", "(GMT +04:00) Port Louis"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Mid-Atlantic Standard Time", "Etc/GMT+2", "(GMT -02:00) Mid-Atlantic"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Middle East Standard Time", "Asia/Beirut", "(GMT +02:00) Beirut"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Montevideo Standard Time", "America/Montevideo", "(GMT -03:00) Montevideo"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Morocco Standard Time", "Africa/Casablanca", "(GMT) Casablanca"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Mountain Standard Time (Mexico)", "America/Chihuahua", "(GMT -07:00) Chihuahua, La Paz, Mazatlan"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Mountain Standard Time", "America/Denver", "(GMT -07:00) Mountain Time (US & Canada)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Myanmar Standard Time", "Asia/Rangoon", "(GMT +06:30) Yangon (Rangoon)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("N. Central Asia Standard Time", "Asia/Novosibirsk", "(GMT +06:00) Novosibirsk"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Namibia Standard Time", "Africa/Windhoek", "(GMT +02:00) Windhoek"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Nepal Standard Time", "Asia/Katmandu", "(GMT +05:45) Kathmandu"));
        ZONEMAPPINGS.add(new TimeZoneMapping("New Zealand Standard Time", "Pacific/Auckland", "(GMT +12:00) Auckland, Wellington"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Newfoundland Standard Time", "America/St_Johns", "(GMT -03:30) Newfoundland"));
        ZONEMAPPINGS.add(new TimeZoneMapping("North Asia East Standard Time", "Asia/Irkutsk", "(GMT +08:00) Irkutsk"));
        ZONEMAPPINGS.add(new TimeZoneMapping("North Asia Standard Time", "Asia/Krasnoyarsk", "(GMT +07:00) Krasnoyarsk"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Pacific SA Standard Time", "America/Santiago", "(GMT -04:00) Santiago"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Pacific Standard Time (Mexico)", "America/Tijuana", "(GMT -08:00) Baja California"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Pacific Standard Time", "America/Los_Angeles", "(GMT -08:00) Pacific Time (US & Canada)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Pakistan Standard Time", "Asia/Karachi", "(GMT +05:00) Islamabad, Karachi"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Paraguay Standard Time", "America/Asuncion", "(GMT -04:00) Asuncion"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Romance Standard Time", "Europe/Paris", "(GMT +01:00) Brussels, Copenhagen, Madrid, Paris"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Russian Standard Time", "Europe/Moscow", "(GMT +03:00) Moscow, St. Petersburg, Volgograd"));
        ZONEMAPPINGS.add(new TimeZoneMapping("SA Eastern Standard Time", "America/Cayenne", "(GMT -03:00) Cayenne, Fortaleza"));
        ZONEMAPPINGS.add(new TimeZoneMapping("SA Pacific Standard Time", "America/Bogota", "(GMT -05:00) Bogota, Lima, Quito"));
        ZONEMAPPINGS.add(new TimeZoneMapping("SA Western Standard Time", "America/La_Paz", "(GMT -04:00) Georgetown, La Paz, Manaus, San Juan"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Samoa Standard Time", "Pacific/Samoa", "(GMT -11:00) Samoa"));
        ZONEMAPPINGS.add(new TimeZoneMapping("SE Asia Standard Time", "Asia/Bangkok", "(GMT +07:00) Bangkok, Hanoi, Jakarta"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Singapore Standard Time", "Asia/Singapore", "(GMT +08:00) Kuala Lumpur, Singapore"));
        ZONEMAPPINGS.add(new TimeZoneMapping("South Africa Standard Time", "Africa/Johannesburg", "(GMT +02:00) Harare, Pretoria"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Sri Lanka Standard Time", "Asia/Colombo", "(GMT +05:30) Sri Jayawardenepura"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Syria Standard Time", "Asia/Damascus", "(GMT +02:00) Damascus"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Taipei Standard Time", "Asia/Taipei", "(GMT +08:00) Taipei"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Tasmania Standard Time", "Australia/Hobart", "(GMT +10:00) Hobart"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Tokyo Standard Time", "Asia/Tokyo", "(GMT +09:00) Osaka, Sapporo, Tokyo"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Tonga Standard Time", "Pacific/Tongatapu", "(GMT +13:00) Nuku'alofa"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Ulaanbaatar Standard Time", "Asia/Ulaanbaatar", "(GMT +08:00) Ulaanbaatar"));
        ZONEMAPPINGS.add(new TimeZoneMapping("US Eastern Standard Time", "America/Indianapolis", "(GMT -05:00) Indiana (East)"));
        ZONEMAPPINGS.add(new TimeZoneMapping("US Mountain Standard Time", "America/Phoenix", "(GMT -07:00) Arizona"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GMT ", "Etc/GMT", "(GMT) Coordinated Universal Time"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GMT +12", "Etc/GMT-12", "(GMT +12:00) Coordinated Universal Time+12"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GMT -02", "Etc/GMT+2", "(GMT -02:00) Coordinated Universal Time-02"));
        ZONEMAPPINGS.add(new TimeZoneMapping("GMT -11", "Etc/GMT+11", "(GMT -11:00) Coordinated Universal Time-11"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Venezuela Standard Time", "America/Caracas", "(GMT -04:30) Caracas"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Vladivostok Standard Time", "Asia/Vladivostok", "(GMT +10:00) Vladivostok"));
        ZONEMAPPINGS.add(new TimeZoneMapping("W. Australia Standard Time", "Australia/Perth", "(GMT +08:00) Perth"));
        ZONEMAPPINGS.add(new TimeZoneMapping("W. Central Africa Standard Time", "Africa/Lagos", "(GMT +01:00) West Central Africa"));
        ZONEMAPPINGS.add(new TimeZoneMapping("W. Europe Standard Time", "Europe/Berlin", "(GMT +01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna"));
        ZONEMAPPINGS.add(new TimeZoneMapping("West Asia Standard Time", "Asia/Tashkent", "(GMT +05:00) Tashkent"));
        ZONEMAPPINGS.add(new TimeZoneMapping("West Pacific Standard Time", "Pacific/Port_Moresby", "(GMT +10:00) Guam, Port Moresby"));
        ZONEMAPPINGS.add(new TimeZoneMapping("Yakutsk Standard Time", "Asia/Yakutsk", "(GMT +09:00) Yakutsk"));
    }

    /**
     * Parse Date to String to give format and timezone
     *
     * @param date
     * @param format
     * @param toTimeZone
     * @return
     */
    public static String formatDateToString(Date date, String format, String toTimeZone) {
        // null check
        if (Objects.isNull(date)) {
            return null;
        }
        // create SimpleDateFormat object
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        // default system timezone if passed null or empty
        if (StringUtils.isBlank(toTimeZone)) {
            toTimeZone = Calendar.getInstance().getTimeZone().getID();
        }
        // set timezone to SimpleDateFormat
        if (StringUtils.isNotBlank(toTimeZone)) {
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone(toTimeZone));
        }
        // set timezone to SimpleDateFormat
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone(toTimeZone));

        // return Date in required format with timezone as String
        String formattedDate = simpleDateFormat.format(date);
        return formattedDate;
    }

    /**
     * Convert Date from One timezone to other
     *
     * @param dateFrom
     * @param fromTimeZone
     * @param toTimeZone
     * @return
     */
    public static Date parseDate(Date dateFrom, String fromTimeZone, String toTimeZone) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        // From Zone format
        SimpleDateFormat sdfFromSimpleDateFormatFrom = new SimpleDateFormat(pattern);
        sdfFromSimpleDateFormatFrom.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
        //To  Zone format
        SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(pattern);
        simpleDateFormatTo.setTimeZone(TimeZone.getTimeZone(toTimeZone));

        Date dateTo = null;
        try {
            dateTo = sdfFromSimpleDateFormatFrom.parse(simpleDateFormatTo.format(dateFrom));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTo;
    }

    /**
     * Format date as string
     *
     * @param dateFrom
     * @param fromTimeZone
     * @param toTimeZone
     * @return
     */
    public static Date parseDateToString(Date dateFrom, String fromTimeZone, String toTimeZone) {
        String pattern = "yyyy/MM/dd HH:mm:ss";
        // From Zone format
        SimpleDateFormat sdfFromSimpleDateFormatFrom = new SimpleDateFormat(pattern);
        sdfFromSimpleDateFormatFrom.setTimeZone(TimeZone.getTimeZone(fromTimeZone));
        //To  Zone format
        SimpleDateFormat simpleDateFormatTo = new SimpleDateFormat(pattern);
        simpleDateFormatTo.setTimeZone(TimeZone.getTimeZone(toTimeZone));
        Date parsedDateTime = null;
        try {
            parsedDateTime = sdfFromSimpleDateFormatFrom.parse(simpleDateFormatTo.format(dateFrom));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedDateTime;
    }

    /**
     * Get Current UTC Date
     *
     * @return Current UTC Date
     */
    public static Date getCurrentDate() {
        ZonedDateTime zonedDateTime = ZonedDateTime.now(ZoneOffset.UTC);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        DateFormat format = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
        String dateStr = zonedDateTime.format(formatter);
        Date utcDate = null;
        try {
            utcDate = format.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return utcDate;

    }

    /**
     * parseDate is used to parse String to Date requested format
     *
     * @param strDate
     * @param format
     * @return
     */
    public static Date parseDate(String strDate, String format) {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = DATE_TIME_FORMAT.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * formatDate is used to format Date to required string format
     *
     * @param inpDate
     * @param format
     * @return
     */
    public static String formatDate(Date inpDate, String format) {
        SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat(format);
        return DATE_TIME_FORMAT.format(inpDate);
    }

    /**
     * formatStringDatetoStringDateTime is used to convert from one format to another format.
     *
     * @param date
     * @return
     */
    public static String formatStringDatetoStringDateTime(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        DateFormat dateTimeFormat = new SimpleDateFormat("yyyy-MM-dd-HH.mm.ss.SSSSSS");
        String dateTime = "";
        try {
            dateTime = dateTimeFormat.format(dateFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * formatStringDateTimetoStringDate is used to convert one format to another format.
     *
     * @param date
     * @param inputFormat
     * @param outputFormat
     * @return
     */
    public static String formatStringDateTimetoStringDate(String date, String inputFormat, String outputFormat) {
        DateFormat dateTimeFormat = new SimpleDateFormat(inputFormat);
        SimpleDateFormat dateFormat = new SimpleDateFormat(outputFormat);
        String dateTime = "";
        try {
            dateTime = dateFormat.format(dateTimeFormat.parse(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateTime;
    }

    /**
     * get Timezone Id based the display names
     * valid formats { (GMT -08:00) Pacific Time (US & Canada),(GMT +05:00) Islamabad, Karachi etc }
     *
     * @param windowsStandardName
     * @return
     */
    public static String getTimeZoneId(String windowsStandardName) {
        if (StringUtils.isNotBlank(windowsStandardName)) {
            Optional<TimeZoneMapping> timeZoneMapping = ZONEMAPPINGS.stream()
                    .filter(m -> m.getDisplayName().equalsIgnoreCase(windowsStandardName.trim())).findFirst();
            if (timeZoneMapping.isPresent()) {
                return timeZoneMapping.get().getZoneId();
            }
        }
        return "";
    }

    /**
     * get Timezone Id based the standard Name
     * valid formats { SA Eastern Standard Time, Pacific Standard Time etc }
     *
     * @param standardName
     * @return
     */
    public static String getTimeZoneIdByStandardName(String standardName) {
        if (StringUtils.isNotBlank(standardName)) {
            Optional<TimeZoneMapping> timeZoneMapping = ZONEMAPPINGS.stream()
                    .filter(m -> m.getStandardName().equalsIgnoreCase(standardName.trim())).findFirst();
            if (timeZoneMapping.isPresent()) {
                return timeZoneMapping.get().getZoneId();
            }
        }
        return "";
    }
}
//    public static void main(String[] args) {
////        Date currentDate = getCurrentDate();
////        System.out.println("DATE -> System Date in UTC: "+parseDate(currentDate, TimeZoneId.UTC, TimeZoneId.UTC));
////        System.out.println("STRING -> System Date in UTC: "+parseDateToString(currentDate, TimeZoneId.UTC, TimeZoneId.UTC));
////
////        System.out.println("DATE -> System Date in PST: "+parseDate(currentDate, TimeZoneId.UTC, TimeZoneId.PST));
////        System.out.println("STRING -> System Date in PST: "+parseDateToString(currentDate, TimeZoneId.UTC, TimeZoneId.PST));
////
////        System.out.println("DATE -> System Date in IST: "+parseDate(currentDate, TimeZoneId.UTC, TimeZoneId.IST));
////        System.out.println("STRING -> System Date in IST: "+parseDateToString(currentDate, TimeZoneId.UTC, TimeZoneId.IST));
//        //Expected Name : America/Los_Angeles
//        String displayName = getTimeZoneIdByStandardName("Pacific Standard Time");
//        System.out.println(displayName);
//        //Expected Name : America/Los_Angeles
//        String windowsDisplayName = getTimeZoneId("(GMT -08:00) Pacific Time (US & Canada)");
//        System.out.println(windowsDisplayName);
//    }
//}

