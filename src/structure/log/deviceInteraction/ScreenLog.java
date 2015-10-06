package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class ScreenLog extends BasicLog {
    private int screenOn;

    public static int privacy = 1;
    public static int energyConsumption = 1;

    public ScreenLog(int id, double timeStamp, long expId, String probeName, int profileId,
                     int screenOn){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.screenOn = screenOn;
    }

    public int get_screenOn() {
        return screenOn;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", screenOn=" + screenOn;
    }
}
