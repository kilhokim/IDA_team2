package structure.log.device;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class AndroidInfoLog extends BasicLog {
    public String buildNumber;
    public String firmwareVersion;
    public int sdk;

    public static int privacy = 1;
    public static int energyConsumption = 1;

    public AndroidInfoLog(int id, double timeStamp, long expId, String probeName, int profileId,
                          String buildNumber, String firmwareVersion, int sdk) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.buildNumber = buildNumber;
        this.firmwareVersion = firmwareVersion;
        this.sdk = sdk;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", buildNumber=" + buildNumber +
                ", firmwareVersion=" + firmwareVersion +
                ", sdk=" + sdk;
    }
}
