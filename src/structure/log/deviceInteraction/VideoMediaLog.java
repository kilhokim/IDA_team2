package structure.log.deviceInteraction;

import structure.log.BasicLog;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class VideoMediaLog extends BasicLog {
    public String displayName;
    public int size;
    public String album;
    public String artist;
    public int bookmark;
    public String bucketDisplayName;
    public int dateAdded;
    public Calendar calendarAdded;
    public int dateTaken;
    public Calendar calendarTaken;
    public int duration;
    public int isPrivate;
    public double latitude;
    public double longitude;
    public String mimeType;
    public String title;
    public double miniThumbMagic;
    public String resolution;

    public static int privacy = 4;
    public static int energyConsumption = 1;

    public VideoMediaLog(int id, double timeStamp, long expId, String probeName, int profileId,
                         String displayName, String size, String album, String artist,
                         int bookmark, String bucketDisplayName, double dateAdded, double dateTaken,
                         int duration, int isPrivate, double latitude, double longitude,
                         String mimeType, String title, double miniThumbMagic, String resolution){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.displayName = displayName;
        this.size = Integer.parseInt(size);
        this.album = album;
        this.artist = artist;
        this.bookmark = bookmark;
        this.bucketDisplayName = bucketDisplayName;
        this.dateAdded = (int) dateAdded;
        if(dateAdded > 0){
            this.calendarAdded = Calendar.getInstance();
            this.calendarAdded.setTime(new Timestamp((long) (dateAdded * 1000)));
        }
        this.dateTaken = (int) dateTaken;
        if(dateTaken > 0){
            this.calendarTaken = Calendar.getInstance();
            this.calendarTaken.setTime(new Timestamp((long) (dateTaken)));
        }
        this.duration = duration;
        this.isPrivate = isPrivate;
        this.latitude = latitude;
        this.longitude = longitude;
        this.mimeType = mimeType;
        this.title = title;
        this.miniThumbMagic = miniThumbMagic;
        this.resolution = resolution;
    }

    public int getYearAdded(){
        return calendarAdded.get(Calendar.YEAR);
    }

    public int getMonthAdded(){
        return calendarAdded.get(Calendar.MONTH);
    }

    public int getDayOfMonthAdded(){
        return calendarAdded.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekAdded(){
        return calendarAdded.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDayAdded(){
        return calendarAdded.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniuteAdded(){
        return calendarAdded.get(Calendar.MINUTE);
    }

    public int getSecondAdded(){
        return calendarAdded.get(Calendar.SECOND);
    }

    public int getMilliSecondAdded(){
        return calendarAdded.get(Calendar.MILLISECOND);
    }

    public int getYearTaken(){
        return calendarTaken.get(Calendar.YEAR);
    }

    public int getMonthTaken(){
        return calendarTaken.get(Calendar.MONTH);
    }

    public int getDayOfMonthTaken(){
        return calendarTaken.get(Calendar.DAY_OF_MONTH);
    }

    public int getDayOfWeekTaken(){
        return calendarTaken.get(Calendar.DAY_OF_WEEK);
    }

    public int getHourOfDayTaken(){
        return calendarTaken.get(Calendar.HOUR_OF_DAY);
    }

    public int getMiniuteTaken(){
        return calendarTaken.get(Calendar.MINUTE);
    }

    public int getSecondTaken(){
        return calendarTaken.get(Calendar.SECOND);
    }

    public int getMilliSecondTaken(){
        return calendarTaken.get(Calendar.MILLISECOND);
    }


    @Override
    public String toString() {
        return super.toString() +
                ", displayName=" + displayName +
                ", size=" + size +
                ", album=" + album +
                ", artist=" + artist +
                ", bookmark=" + bookmark +
                ", dateAdded=" + dateAdded +
                ", dateTaken=" + dateTaken +
                ", duration=" + duration +
                ", isPrivate=" + isPrivate +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                ", mimeType=" + mimeType +
                ", title=" + title +
                ", miniThumbMagic=" + miniThumbMagic +
                ", resolution=" + resolution;
    }
}
