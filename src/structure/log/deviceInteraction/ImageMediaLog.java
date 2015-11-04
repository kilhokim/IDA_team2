package structure.log.deviceInteraction;

import structure.log.BasicLog;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class ImageMediaLog extends BasicLog {
    public String displayName;
    public long size;
    public String bucketDisplayName;
    public int dateAdded;
    public Calendar calendarAdded;
    public int dateTaken;
    public Calendar calendarTaken;
    public int isPrivate;
    public double latitude;
    public double longitude;
    public String mimeType;
    public String title;
    public double miniThumbMagic;
    public int orientation;

    public static int privacy = 4;
    public static int energyConsumption = 1;

    public ImageMediaLog(int id, double timeStamp, long expId, String probeName, int profileId,
                         String displayName, long size, String bucketDisplayName,
                         double dateAdded, double dateTaken, int isPrivate, double latitude,
                         double longitude, String mimeType, String title,
                         double miniThumbMagic, int orientation) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.displayName = displayName;
        this.size = size;
        this.bucketDisplayName = bucketDisplayName;
        this.dateAdded = (int) dateAdded;
        if (dateAdded > 0) {
            this.calendarAdded = Calendar.getInstance();
            this.calendarAdded.setTime(new Timestamp((long) (dateAdded * 1000)));
        }
        this.dateTaken = (int) dateTaken;
        if (dateTaken > 0) {
            this.calendarTaken = Calendar.getInstance();
            this.calendarTaken.setTime(new Timestamp((long) (dateTaken)));
        }
        this.isPrivate = isPrivate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mimeType = mimeType;
        this.title = title;
        this.miniThumbMagic = miniThumbMagic;
        this.orientation = orientation;
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

    public int getYearTaken() {
        return calendarTaken.get(Calendar.YEAR);
    }

    public int getMonthTaken() {
        return calendarTaken.get(Calendar.MONTH);
    }

    public int getDayOfMonthTaken() {
        return calendarTaken.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekTaken() {
        return calendarTaken.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDayTaken() {
        return calendarTaken.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniuteTaken() {
        return calendarTaken.get(Calendar.MINUTE);
    }

    public int getSecondTaken() {
        return calendarTaken.get(Calendar.SECOND);
    }

    public int getMilliSecondTaken() {
        return calendarTaken.get(Calendar.MILLISECOND);
    }

    @Override
    public String toString() {
        return super.toString() +
                ", displayName=" + displayName +
                ", size=" + size +
                ", bucketDisplayName=" + bucketDisplayName +
                ", dateAdded=" + dateAdded +
                ", dateTaken=" + dateTaken +
                ", isPrivate=" + isPrivate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", mimeType=" + mimeType +
                ", title=" + title +
                ", miniThumbMagic=" + miniThumbMagic +
                ", orientation=" + orientation;
    }
}
