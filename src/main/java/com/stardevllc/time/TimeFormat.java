package com.stardevllc.time;

import java.text.DecimalFormat;
import java.util.*;

/**
 * Allows formatting a time in milliseconds similar to how the Java NumberFormat works. 
 * This does use the DecimalFormat class for formatting the numbers themselves
 * This uses the {@link TimeUnit} class to parse time arguments. You can use any of the aliases in that class to denote the unit to parse
 * All time formats must be encased in opening and closing % symbols. 
 * The alias used in the format will also be the one used as suffix after the numeric time
 * Use a * after the first % sign to have it not show if the value for that unit is a 0
 * Example: %00s% will format 5000 as 05s. 
 */
public class TimeFormat {
    private static final Comparator<TimeUnit> UNIT_COMPARATOR = (o1, o2) -> {
        if (o1.ordinal() < o2.ordinal()) {
            return 1;
        } else if (o1.ordinal() == o2.ordinal()) {
            return 0;
        }
        return -1;
    };

    public static final Set<TimeUnit> UNIT_ORDER = new TreeSet<>(UNIT_COMPARATOR);

    static {
        UNIT_ORDER.addAll(Arrays.asList(TimeUnit.values()));
    }

    private String pattern;
    private final Map<TimeUnit, TimePattern> unitPatterns = new EnumMap<>(TimeUnit.class);

    /**
     * Constructs a new time format
     * @param pattern The pattern to use for the format
     */
    public TimeFormat(String pattern) {
        this.pattern = pattern;
        parsePattern();
    }

    /**
     * Formats the time based on the pattern
     * @param time The time in milliseconds to format
     * @return The formatted time
     */
    public String format(long time) {
        String formattedTime = this.pattern;
        long totalTime = time;
        for (TimeUnit unit : UNIT_ORDER) {
            if (!unitPatterns.containsKey(unit)) {
                continue;
            }

            long unitLength = (long) unit.fromMillis(totalTime);
            //totalTime -= unit.toMillis(unitLength);
            totalTime = totalTime % unit.getMsPerUnit();

            TimePattern timePattern = unitPatterns.get(unit);
            String value;
            if (unitLength == 0 && !timePattern.showIfZero()) {
                value = "";
            } else {
                value = new DecimalFormat(timePattern.numberPattern().replace("*", "")).format(unitLength) + timePattern.unitPattern();
            }
            formattedTime = formattedTime.replace("%" + timePattern.numberPattern() + timePattern.unitPattern() + "%", value);
        }

        return formattedTime;
    }

    /**
     * Sets the pattern to be used.
     * @param pattern The new pattern string
     */
    public void setPattern(String pattern) {
        this.unitPatterns.clear();
        this.pattern = pattern;
        parsePattern();
    }

    private void parsePattern() {
        int startIndex = -1, endIndex = -1;
        StringBuilder patternBuilder = new StringBuilder();
        StringBuilder unitBuilder = new StringBuilder();
        boolean showIfZero = true;

        for (int i = 0; i < this.pattern.length(); i++) {
            char c = this.pattern.charAt(i);
            if (c == '%') {
                if (startIndex == -1) {
                    startIndex = i;
                    continue;
                } else if (endIndex == -1) {
                    endIndex = i;
                }
            }

            if (startIndex != -1 && endIndex != -1) {
                TimeUnit unit = TimeUnit.matchUnit(unitBuilder.toString().trim());
                if (unit == null) {
                    continue;
                }

                if (patternBuilder.toString().startsWith("*")) {
                    showIfZero = false;
                }

                unitPatterns.put(unit, new TimePattern(unitBuilder.toString(), patternBuilder.toString(), showIfZero));
                startIndex = -1;
                endIndex = -1;
                patternBuilder = new StringBuilder();
                unitBuilder = new StringBuilder();
                showIfZero = true;
            } else if (startIndex != -1) {
                if (c == '0' || c == '#' || c == '*') {
                    patternBuilder.append(c);
                } else {
                    unitBuilder.append(c);
                }
            }
        }
    }

    record TimePattern(String unitPattern, String numberPattern, boolean showIfZero) {}
}