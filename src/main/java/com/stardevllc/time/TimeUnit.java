package com.stardevllc.time;

public enum TimeUnit {
    MILLISECONDS(1, "millisecond", "ms"),
    TICKS(50, "tick", "t"),
    SECONDS(1000, "second", "s") ,
    MINUTES(SECONDS.msPerUnit * 60, "minute", "min", "m"),
    HOURS(MINUTES.msPerUnit * 60, "hour", "h"),
    DAYS(HOURS.msPerUnit * 24, "day", "d"),
    /**
     * The {@code WEEKS} value is based on a 7.604166666666667-day week. This is the average weeks in a month based on the below month value
     */
    WEEKS((long) (DAYS.msPerUnit * 7.604166666666667), "week", "w"),
    /**
     * The {@code MONTHS} value is based on a 30.41666667-day month. This is the average days in a month based on a 365 day year
     */
    MONTHS((long) (DAYS.msPerUnit * 30.41666667), "month", "mo"),
    /**
     * The {@code YEARS} value is based on a 365-day year. If another day count is needed, convert from the {@code DAYS} enum value.
     */
    YEARS(DAYS.msPerUnit * 365, "year", "y");
    
    private final String name;
    private final String[] aliases;
    private final long msPerUnit;

    TimeUnit(long msPerUnit, String... aliases) {
        this.msPerUnit = msPerUnit;
        this.name = name().toLowerCase();
        this.aliases = aliases;
    }

    public String getName() {
        return name;
    }

    public String[] getAliases() {
        return aliases;
    }

    public double fromMillis(long milliseconds) {
        return milliseconds / (this.msPerUnit * 1.0);
    }

    public long toMillis(long duration) {
        return msPerUnit * duration;
    }

    public double toTicks(long duration) {
        return toMillis(duration) / (TICKS.toMillis(1) * 1.0);
    }

    public double toSeconds(long duration) {
        return toMillis(duration) / (SECONDS.toMillis(1) * 1.0);
    }

    public double toMinutes(long duration) {
        return toMillis(duration) / (MINUTES.toMillis(1) * 1.0);
    }

    public double toHours(long duration) {
        return toMillis(duration) / (HOURS.toMillis(1) * 1.0);
    }

    public double toDays(long duration) {
        return toMillis(duration) / (DAYS.toMillis(1) * 1.0);
    }

    public double toWeeks(long duration) {
        return toMillis(duration) / (WEEKS.toMillis(1) * 1.0);
    }

    public double toMonths(long duration) {
        return toMillis(duration) / (MONTHS.toMillis(1) * 1.0);
    }

    public double toYears(long duration) {
        return toMillis(duration) / (YEARS.toMillis(1) * 1.0);
    }

    public long getMsPerUnit() {
        return msPerUnit;
    }

    public static TimeUnit matchUnit(String unitString) {
        for (TimeUnit unit : values()) {
            if (unit.getName().equalsIgnoreCase(unitString)) {
                return unit;
            }

            for (String alias : unit.getAliases()) {
                if (alias.equalsIgnoreCase(unitString)) {
                    return unit;
                }
            }
        }

        return null;
    }
}