package structure.log.deviceInteraction;

import structure.log.BasicLog;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class AudioMediaLog extends BasicLog {
    public String displayName;
    public int size;
    public String album;
    public String artist;
    public String composer;
    public int dateAdded;
    public Calendar calendarAdded;
    public int duration;
    public int isAlarm;
    public int isMusic;
    public int isNotification;
    public int isRingtone;
    public String mimeType;
    public String title;
    public int track;
    public int year;

    public static int privacy = 4;
    public static int energyConsumption = 1;

    public AudioMediaLog(int id, double timeStamp, long expId, String probeName, int profileId,
                         String displayName, String size, String album, String artist,
                         String composer, double dateAdded, int duration, int isAlarm,
                         int isMusic, int isNotification, int isRingtone, String mimeType,
                         String title, int track, String year) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.displayName = displayName;
        this.size = Integer.parseInt(size);
        this.album = album;
        this.artist = artist;
        this.composer = composer;
        this.dateAdded = (int) dateAdded;
        if (dateAdded > 0) {
            this.calendarAdded = Calendar.getInstance();
            this.calendarAdded.setTime(new Timestamp((long) (dateAdded * 1000)));
        }
        this.duration = duration;
        this.isAlarm = isAlarm;
        this.isMusic = isMusic;
        this.isNotification = isNotification;
        this.isRingtone = isRingtone;
        this.mimeType = mimeType;
        this.title = title;
        this.track = track;
        this.year = Integer.parseInt(year);
    }

    public int getYearAdded() {
        return calendarAdded.get(Calendar.YEAR);
    }

    public int getMonthAdded() {
        return calendarAdded.get(Calendar.MONTH);
    }

    public int getDayOfMonthAdded() {
        return calendarAdded.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekAdded() {
        return calendarAdded.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDayAdded() {
        return calendarAdded.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniuteAdded() {
        return calendarAdded.get(Calendar.MINUTE);
    }

    public int getSecondAdded() {
        return calendarAdded.get(Calendar.SECOND);
    }

    public int getMilliSecondAdded() {
        return calendarAdded.get(Calendar.MILLISECOND);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", displayName=" + displayName +
                ", size=" + size +
                ", album=" + album +
                ", artist=" + artist +
                ", composer=" + composer +
                ", dateAdded=" + dateAdded +
                ", duration=" + duration +
                ", isAlarm=" + isAlarm +
                ", isMusic=" + isMusic +
                ", isNotification=" + isNotification +
                ", isRingtone=" + isRingtone +
                ", mimeType=" + mimeType +
                ", title=" + title +
                ", track=" + track +
                ", year=" + year;
    }
}
