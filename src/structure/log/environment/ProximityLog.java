package structure.log.environment;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class ProximityLog extends BasicLog {
    public double distance;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public ProximityLog(int id, double timeStamp, long expId, String probeName, int profileId,
                        double distance) {
        super(id, timeStamp, expId, probeName, profileId);
        this.distance = distance;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", distance=" + distance;
    }
}
