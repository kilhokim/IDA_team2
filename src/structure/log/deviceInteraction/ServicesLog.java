package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-09-24.
 */
public class ServicesLog extends BasicLog {
    public String process;

    public static int privacy = 3;
    public static int energyConsumption = 1;

    public ServicesLog(int id, double timeStamp, long expId, String probeName, int profileId,
                       String process) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.process = process;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", process=" + process;
    }
}
