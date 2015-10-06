package structure.log.motion;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class OrientationLog extends BasicLog {
    public int accuracy;
    public double azimuth;
    public double pitch;
    public double roll;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public OrientationLog(int id, double timeStamp, long expId, String probeName, int profileId,
                          int accuracy, double azimuth, double pitch, double roll) {
        super(id, timeStamp, expId, probeName, profileId);
        this.accuracy = accuracy;
        this.azimuth = azimuth;
        this.pitch = pitch;
        this.roll = roll;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accuracy" + accuracy +
                ", azimuth=" + azimuth +
                ", pitch=" + pitch +
                ", roll=" + roll;
    }
}
