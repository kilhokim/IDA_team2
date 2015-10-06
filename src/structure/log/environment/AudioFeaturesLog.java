package structure.log.environment;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class AudioFeaturesLog extends BasicLog {
    public double diffSecs;
    public double l1Norm;
    public double l2Norm;
    public double linfNorm;

    public static int privacy = 2;
    public static int energyConsumption = 2;

    public AudioFeaturesLog(int id, double timeStamp, long expId, String probeName, int profileId,
                            double diffSecs, double l1Norm,
                            double l2Norm, double linfNorm) {
        super(id, timeStamp, expId, probeName, profileId);
        this.diffSecs = diffSecs;
        this.l1Norm = l1Norm;
        this.l2Norm = l2Norm;
        this.linfNorm = linfNorm;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", diffSecs=" + diffSecs +
                ", l1Norm=" + l1Norm +
                ", l2Norm=" + l2Norm +
                ", linfNorm=" + linfNorm;
    }
}
