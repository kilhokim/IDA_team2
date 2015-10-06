package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-09-24.
 */
public class ProcessStatisticsLog extends BasicLog {
    public String runningProcessInfo;

    public static int privacy = 2;
    public static int energyConsumption = 1;

    public ProcessStatisticsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                                String runningProcessInfo) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.runningProcessInfo = runningProcessInfo;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", runningProcessInfo=" + runningProcessInfo;
    }
}
