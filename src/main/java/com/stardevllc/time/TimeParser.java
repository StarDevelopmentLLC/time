package com.stardevllc.time;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Stream;

public class TimeParser {
    public long parseTime(String rawTime) {
        ParsedTime years = extractRawTime(rawTime, TimeUnit.YEARS);
        ParsedTime months = extractRawTime(years.leftOverInput(), TimeUnit.MONTHS);
        ParsedTime weeks = extractRawTime(months.leftOverInput(), TimeUnit.WEEKS);
        ParsedTime days = extractRawTime(weeks.leftOverInput(), TimeUnit.DAYS);
        ParsedTime hours = extractRawTime(days.leftOverInput(), TimeUnit.HOURS);
        ParsedTime minutes = extractRawTime(hours.leftOverInput(), TimeUnit.MINUTES);
        ParsedTime seconds = extractRawTime(minutes.leftOverInput(), TimeUnit.SECONDS);
        return Stream.of(years, months, weeks, days, hours, minutes, seconds).mapToLong(ParsedTime::timeInMillis).sum();
    }
    
    public long parseDate(String rawDate) throws IllegalArgumentException {
        ParsedDate parsedDate = parseRawDate(rawDate);
        if (parsedDate == null) {
            return 0;
        }
        ZonedDateTime zonedDateTime = ZonedDateTime.of(parsedDate.getYear(), parsedDate.getMonth(), parsedDate.getDay(), parsedDate.getHour(), parsedDate.getMinute(), parsedDate.getSecond(), 0, ZoneOffset.UTC);
        return zonedDateTime.toInstant().toEpochMilli();
    }
    
    public ParsedDate parseRawDate(String rawDate) throws IllegalArgumentException {
        String[] inputArray = rawDate.split(" ");
        if (inputArray.length < 1) {
            return null;
        }

        String[] rawDateArray = inputArray[0].split("/");
        if (!(rawDateArray.length >= 3)) {
            throw new IllegalArgumentException("Invalid day of year arguments");
        }
        short month = parseTimeArgument(rawDateArray[0]), day = parseTimeArgument(rawDateArray[1]), year = parseTimeArgument(rawDateArray[2]);

        if (month == -2 || day == -2 || year == -2) {
            return null;
        }

        short hour, minute, second;

        if (inputArray.length == 2) {
            String[] rawTimeArray = inputArray[1].split(":");

            hour = parseTimeArgument(rawTimeArray[0]);
            minute = parseTimeArgument(rawTimeArray[1]);
            second = parseTimeArgument(rawTimeArray[2]);

            if (hour == -2 || minute == -2 || second == -2) {
                return null;
            }
        } else {
            hour = 0;
            minute = 0;
            second = 0;
        }

        return new ParsedDate().setMonth(month).setDay(day).setYear(year).setHour(hour).setMinute(minute).setSecond(second);
    }

    private short parseTimeArgument(String arg) {
        try {
            return Short.parseShort(arg);
        } catch (NumberFormatException e) {
            return -2;
        }
    }

    private ParsedTime extractRawTime(String rawTime, TimeUnit unit) {
        rawTime = rawTime.toLowerCase();
        String[] rawArray;
        for (String alias : unit.getAliases()) {
            alias = alias.toLowerCase();
            if (rawTime.contains(alias)) {
                rawArray = rawTime.split(alias);
                String fh = rawArray[0];
                long rawLength;
                try {
                    rawLength = Integer.parseInt(fh);
                } catch (NumberFormatException e) {
                    StringBuilder sb = new StringBuilder();
                    for (int i = fh.length() - 1; i > 0; i--) {
                        char c = fh.charAt(i);
                        if (Character.isDigit(c)) {
                            sb.insert(0, c);
                        } else {
                            break;
                        }
                    }
                    rawLength = Integer.parseInt(sb.toString());
                }
                rawTime = rawTime.replace(rawLength + alias, "");
                return new ParsedTime(unit.toMillis(rawLength), rawTime);
            }
        }

        return new ParsedTime(0L, rawTime);
    }
    
    private record ParsedTime(long timeInMillis, String leftOverInput) {}

    public static class ParsedDate {
        private int year, month, day, hour, minute, second;

        public int getYear() {
            return year;
        }

        public ParsedDate setYear(int year) {
            this.year = year;
            return this;
        }

        public int getMonth() {
            return month;
        }

        public ParsedDate setMonth(int month) {
            this.month = month;
            return this;
        }

        public int getDay() {
            return day;
        }

        public ParsedDate setDay(int day) {
            this.day = day;
            return this;
        }

        public int getHour() {
            return hour;
        }

        public ParsedDate setHour(int hour) {
            this.hour = hour;
            return this;
        }

        public int getMinute() {
            return minute;
        }

        public ParsedDate setMinute(int minute) {
            this.minute = minute;
            return this;
        }

        public int getSecond() {
            return second;
        }

        public ParsedDate setSecond(int second) {
            this.second = second;
            return this;
        }

        @Override
        public String toString() {
            return "ParsedDate{" +
                    "year=" + year +
                    ", month=" + month +
                    ", day=" + day +
                    ", hour=" + hour +
                    ", minute=" + minute +
                    ", second=" + second +
                    '}';
        }
    }
}