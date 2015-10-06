package structure.log.environment;

import structure.log.BasicLog;

public class MagneticFieldLog extends BasicLog {
    public int accuracy;
    public double x;
    public double y;
    public double z;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public MagneticFieldLog(int id, double timeStamp, long expId, String probeName, int profileId,
                            int accuracy, double x, double y, double z) {
        super(id, timeStamp, expId, probeName, profileId);
        this.accuracy = accuracy;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accuracy=" + accuracy +
                ", x=" + x +
                ", y=" + y +
                ", z=" + z;
    }
}
