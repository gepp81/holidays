package ar.com.gep.holydays.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

import java.util.Map;

public class WeekCounter {
    private int saturday;
    private int sunday;
    private int holiday;
    private Map<Long, Object> holidays;

    public WeekCounter() {
        this.saturday = 0;
        this.sunday = 0;
        holiday = 0;
    }

    public int getHoliday() {
        return holiday;
    }

    public int getSaturday() {
        return saturday;
    }

    public int getSunday() {
        return sunday;
    }

    public void setHolidays(Map<Long, Object> holidays) {
        this.holidays = holidays;
    }

    /**
     * @param startDate
     */
    private void addDay(DateTime startDate) {
        switch (startDate.dayOfWeek().get()) {
            case DateTimeConstants.SATURDAY:
                saturday++;
                break;
            case DateTimeConstants.SUNDAY:
                sunday++;
                break;
            default:
                if (holidays.containsKey(startDate.getMillis())) {
                    holiday++;
                }
                break;
        }
    }

    /**
     * @param startDate
     * @param endDate
     */
    public void count(DateTime startDate, DateTime endDate) {
        while (startDate.isBefore(endDate)) {
            addDay(startDate);
            startDate = startDate.plusDays(1);
        }
        addDay(endDate);
    }

}