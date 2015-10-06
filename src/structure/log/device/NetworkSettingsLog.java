package structure.log.device;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class NetworkSettingsLog extends BasicLog {
    public int airPlaneModeOn;
    public int mobileDataOn;
    public int wifiOn;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public NetworkSettingsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                              int airPlaneModeOn, int mobileDataOn, int wifiOn) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.airPlaneModeOn = airPlaneModeOn;
        this.mobileDataOn = mobileDataOn;
        this.wifiOn = wifiOn;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", airPlaneModeOn=" + airPlaneModeOn +
                ", mobileDataOn=" + mobileDataOn +
                ", wifiOn=" + wifiOn;
    }
}
