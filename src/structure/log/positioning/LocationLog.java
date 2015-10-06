package structure.log.positioning;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-09-24.
 */
public class LocationLog extends BasicLog {
    public double mAccuracy;
    public double mAltitude;
    public double mBearing;
    public double mLatitude;
    public double mLongitude;
    public double mSpeed;
    public String mProvider;
    public int mHasAccuracy;
    public int mHasAltitude;
    public int mHasBearing;
    public int mHasSpeed;
    public int mIsFromMockProvider;

    public static int privacy = 3;
    public static int energyConsumption = 5;

    public LocationLog(int id, double timeStamp, long expId, String probeName, int profileId,
                       double mAccuracy, double mAltitude, double mBearing, double mLatitude, double mLongitude,
                       double mSpeed, String mProvider, int mHasAccuracy, int mHasAltitude, int mHasBearing,
                       int mHasSpeed, int mIsFromMockProvider) {
        super(id, timeStamp, expId, probeName, profileId);
        this.mAccuracy = mAccuracy;
        this.mAltitude = mAltitude;
        this.mBearing = mBearing;
        this.mLatitude = mLatitude;
        this.mLongitude = mLongitude;
        this.mSpeed = mSpeed;
        this.mProvider = mProvider;
        this.mHasAccuracy = mHasAccuracy;
        this.mHasAltitude = mHasAltitude;
        this.mHasBearing = mHasBearing;
        this.mHasSpeed = mHasSpeed;
        this.mIsFromMockProvider = mIsFromMockProvider;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", mAccuracy=" + mAccuracy +
                ", mAltitude=" + mAltitude +
                ", mBearing=" + mBearing +
                ", mLatitude=" + mLatitude +
                ", mLongitude=" + mLongitude +
                ", mSpeed=" + mSpeed +
                ", mProvider=" + mProvider +
                ", mSpeed=" + mHasAccuracy +
                ", mSpeed=" + mHasAltitude +
                ", mSpeed=" + mHasBearing +
                ", mSpeed=" + mHasSpeed +
                ", mSpeed=" + mIsFromMockProvider;
    }
}
