package structure.log.positioning;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class SimpleLocationLog extends BasicLog {
    public double mAccuracy;
    public double mLatitude;
    public double mLongitude;
    public String mProvider;

    public static int privacy = 2;
    public static int energyConsumption = 4;

    public SimpleLocationLog(int id, double timeStamp, long expId, String probeName, int profileId,
                             double mAccuracy, double mLatitude, double mLongitude, String mProvider){
        super(id, timeStamp, expId, probeName, profileId);

        this.mAccuracy = mAccuracy;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mProvider = mProvider;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", mAccuracy=" + mAccuracy +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mProvider=" + mProvider;
    }
}
