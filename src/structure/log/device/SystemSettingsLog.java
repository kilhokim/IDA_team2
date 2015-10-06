package structure.log.device;

import structure.log.BasicLog;

import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by Ethan on 2015-08-28.
 */
public class SystemSettingsLog extends BasicLog {
    public int accelerometerRotation;
    public int screenBrightness;
    public int volumeAlarm;
    public int volumeMusic;
    public int volumeNotification;
    public int volumeRing;
    public int volumeSystem;
    public int volumeVoice;
    public Calendar calendar;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public SystemSettingsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                             int accelerometerRotation, int screenBrightness,
                             int volumeAlarm, int volumeMusic, int volumeNotification,
                             int volumeRing, int volumeSystem, int volumeVoice) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.accelerometerRotation = accelerometerRotation;
        this.screenBrightness = screenBrightness;
        this.volumeAlarm = volumeAlarm;
        this.volumeMusic = volumeMusic;
        this.volumeNotification = volumeNotification;
        this.volumeRing = volumeRing;
        this.volumeSystem = volumeSystem;
        this.volumeVoice = volumeVoice;
        if (timeStamp > 0) {
            this.calendar = Calendar.getInstance();
            this.calendar.setTime(new Timestamp((long) (timeStamp * 1000)));
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accelerometerRotation=" + accelerometerRotation +
                ", screenBrightness=" + screenBrightness +
                ", volumeAlarm=" + volumeAlarm +
                ", volumeMusic=" + volumeMusic +
                ", volumeNotification=" + volumeNotification +
                ", volumeRing=" + volumeRing +
                ", volumeSystem=" + volumeSystem +
                ", volumeVoice=" + volumeVoice +
                ", calendar=" + calendar;
    }
}
