package structure.log.environment;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class PressureLog extends BasicLog {
    public int accuracy;
    public double pressure;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public PressureLog(int id, double timeStamp, long expId, String probeName, int profileId,
                       int accuracy, double pressure) {
        super(id, timeStamp, expId, probeName, profileId);
        this.accuracy = accuracy;
        this.pressure = pressure;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accuracy" + accuracy +
                ", pressure=" + pressure;
    }
}
