package structure.log.motion;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class RotationVectorLog extends BasicLog {
    public int accuracy;
    public double cosThetaOver2;
    public double xSinThetaOver2;
    public double ySinThetaOver2;
    public double zSinThetaOver2;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public RotationVectorLog(int id, double timeStamp, long expId, String probeName, int profileId,
                             int accuracy, double cosThetaOver2, double xSinThetaOver2,
                             double ySinThetaOver2, double zSinThetaOver2) {
        super(id, timeStamp, expId, probeName, profileId);
        this.accuracy = accuracy;
        this.cosThetaOver2 = cosThetaOver2;
        this.xSinThetaOver2 = xSinThetaOver2;
        this.ySinThetaOver2 = ySinThetaOver2;
        this.zSinThetaOver2 = zSinThetaOver2;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", accuracy=" + accuracy +
                ", cosThetaOver2=" + cosThetaOver2 +
                ", xSinThetaOver2=" + xSinThetaOver2 +
                ", ySinThetaOver2=" + ySinThetaOver2 +
                ", zSinThetaOver2=" + zSinThetaOver2;
    }
}
