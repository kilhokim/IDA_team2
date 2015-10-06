package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class BrowserSearchesLog extends BasicLog {
    public String search;

    public static int privacy = 5;
    public static int energyConsumption = 1;

    public BrowserSearchesLog(int id, double timeStamp, long expId, String probeName, int profileId,
                              String search) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.search = search;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", search=" + search;
    }
}
