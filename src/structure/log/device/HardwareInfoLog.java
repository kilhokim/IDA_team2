package structure.log.device;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class HardwareInfoLog extends BasicLog {
    public String androidId;
    public String bluetoothMac;
    public String brand;
    public int deviceId;
    public String model;
    public String wifiMac;

    public static int privacy = 2;
    public static int energyConsumption = 1;

    public HardwareInfoLog(int id, double timeStamp, long expId, String probeName, int profileId,
                           String androidId, String bluetoothMac, String brand,
                           double deviceId, String model, String wifiMac) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 86400;
        super.scheduleDuration = 0;

        this.androidId = androidId;
        this.bluetoothMac = bluetoothMac;
        this.brand = brand;
        this.deviceId = (int) deviceId;
        this.model = model;
        this.wifiMac = wifiMac;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", androidId=" + androidId +
                ", bluetoothMac=" + bluetoothMac +
                ", brand=" + brand +
                ", deviceId=" + deviceId +
                ", model=" + model +
                ", wifiMac=" + wifiMac;
    }
}
