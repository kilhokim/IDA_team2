package structure.log.device;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class BatteryLog extends BasicLog {
    public int health;
    public int level;
    public int plugged;
    public int present;
    public int scale;
    public String technology;
    public int status;
    public int temperature;
    public int voltage;

    public static int privacy = 1;
    public static int energyConsumption = 2;

    public BatteryLog(int id, double timeStamp, long expId, String probeName, int profileId,
                      int health, int level, int plugged, int present,
                      int scale, String technology, int status,
                      int temperature, int voltage) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 1800;
        super.scheduleDuration = 0;

        this.health = health;
        this.level = level;
        this.plugged = plugged;
        this.present = present;
        this.scale = scale;
        this.technology = technology;
        this.status = status;
        this.temperature = temperature;
        this.voltage = voltage;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", health=" + health +
                ", level=" + level +
                ", plugged=" + plugged +
                ", present=" + present +
                ", scale=" + scale +
                ", technology=" + technology +
                ", status=" + status +
                ", temperature=" + temperature +
                ", voltage=" + voltage;
    }
}
