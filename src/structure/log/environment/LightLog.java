package structure.log.environment;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class LightLog extends BasicLog {
    public int accuracy;
    public double lux;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public LightLog(int id, double timeStamp, long expId, String probeName, int profileId,
                    int accuracy, double lux) {
        super(id, timeStamp, expId, probeName, profileId);
        this.accuracy = accuracy;
        this.lux = lux;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accuracy" + accuracy +
                ", lux=" + lux;
    }
}
