package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class AccountsLog extends BasicLog {
    public String type;

    public static int privacy = 2;
    public static int energyConsumption = 1;

    public AccountsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                       String type) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", type=" + type;
    }
}
