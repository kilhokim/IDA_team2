package structure.log.deviceInteraction;

import structure.log.BasicLog;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class BrowserBookmarksLog extends BasicLog {
    public int bookmark;
    public int created;
    public Calendar calendarCreated;
    public String title;
    public String url;
    public int visits;

    public static int privacy = 5;
    public static int energyConsumption = 1;

    public BrowserBookmarksLog(int id, double timeStamp, long expId, String probeName, int profileId,
                               int bookmark, double created, String title, String url, int visits) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.bookmark = bookmark;
        this.created = (int) created;
        if (created > 0) {
            this.calendarCreated = Calendar.getInstance();
            this.calendarCreated.setTime(new Timestamp((long) (created)));
        }
        this.title = title;
        this.url = url;
        this.visits = visits;
    }

    public int getYearCreated() {
        return calendarCreated.get(Calendar.YEAR);
    }

    public int getMonthCreated() {
        return calendarCreated.get(Calendar.MONTH);
    }

    public int getDayOfMonthCreated() {
        return calendarCreated.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekCreated() {
        return calendarCreated.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDayCreated() {
        return calendarCreated.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniuteCreated() {
        return calendarCreated.get(Calendar.MINUTE);
    }

    public int getSecondCreated() {
        return calendarCreated.get(Calendar.SECOND);
    }

    public int getMilliSecondCreated() {
        return calendarCreated.get(Calendar.MILLISECOND);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", bookmark=" + bookmark +
                ", created=" + created +
                ", title=" + title +
                ", url=" + url +
                ", visits=" + visits;
    }
}
