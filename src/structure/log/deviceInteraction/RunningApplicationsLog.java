package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-09-05.
 */
public class RunningApplicationsLog extends BasicLog {
    public double duration;
    public String mClass;
    public String mPackage;
    public String appCategory;
    public String appName;
    public int appPrice;

    public static int privacy = 3;
    public static int energyConsumption = 2;

    public RunningApplicationsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                                  double duration, String mClass, String mPackage, String appCategory,
                                  String appName, int appPrice) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.duration = duration;
        this.mClass = mClass;
        this.mPackage = mPackage;
        this.appCategory = appCategory;
        this.appName = appName;
        this.appPrice = appPrice;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", duration=" + duration +
                ", mClass='" + mClass + '\'' +
                ", mPackage='" + mPackage + '\'' +
                ", appCategory='" + appCategory + '\'' +
                ", appName='" + appName + '\'' +
                ", appPrice=" + appPrice;
    }
}
