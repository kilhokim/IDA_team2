package structure.log.deviceInteraction;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class ApplicationsLog extends BasicLog {
    public String packageName;
    public String processName;
    public int targetSdkVersion;
    public String taskAffinity;
    public String category;
    public String appCategory;
    public String appName;
    public int appPrice;

    public static int privacy = 3;
    public static int energyConsumption = 1;

    public ApplicationsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                           String packageName, String processName, double targetSdkVersion,
                           String taskAffinity, String category, String appCategory, String appName, int appPrice) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.packageName = packageName;
        this.processName = processName;
        this.targetSdkVersion = (int) targetSdkVersion;
        this.taskAffinity = taskAffinity;
        this.category = category;
        this.appCategory = appCategory;
        this.appName = appName;
        this.appPrice = appPrice;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", packageName=" + packageName +
                ", processName=" + processName +
                ", targetSdkVersion=" + targetSdkVersion +
                ", taskAffinity=" + taskAffinity +
                ", category=" + category +
                ", appCategory=" + appCategory +
                ", appName=" + appName +
                ", appPrice=" + appPrice;
    }
}
