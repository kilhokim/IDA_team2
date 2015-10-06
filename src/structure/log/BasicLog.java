package structure.log;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class BasicLog {
    public int id;
    public double timeStamp;
    public long expId;
    public Calendar calendar;
    public String probeName;
    public int profileId;

    public int scheduleInterval = 900;
    public int scheduleDuration = 180;

    public BasicLog(int id, double timeStamp, long expId, String probeName, int profileId) {
        this.id = id;
        this.timeStamp = timeStamp;
        this.expId = expId;
        this.calendar = Calendar.getInstance();
        this.calendar.setTime(new Timestamp((long) (timeStamp * 1000)));
        this.probeName = probeName;
        this.profileId = profileId;
    }

    public int getYear() {
        return calendar.get(Calendar.YEAR);
    }

    public int getMonth() {
        return calendar.get(Calendar.MONTH);
    }

    public int getDayOfMonth() {
        return calendar.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeek() {
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDay() {
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniute() {
        return calendar.get(Calendar.MINUTE);
    }

    public int getSecond() {
        return calendar.get(Calendar.SECOND);
    }

    public int getMilliSecond() {
        return calendar.get(Calendar.MILLISECOND);
    }

    @Override
    public String toString() {
        return "id=" + id +
                ", timeStamp=" + timeStamp +
                ", expId=" + expId +
                ", probeName='" + probeName + '\'' +
                ", profileId=" + profileId;
    }
}
