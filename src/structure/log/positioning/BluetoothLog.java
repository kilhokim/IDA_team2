package structure.log.positioning;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-09-05.
 */
public class BluetoothLog extends BasicLog {
    public String appearance;
    public String class_mClass;
    public String device_mAddress;
    public String device_mRemoteBrsf;
    public String device_mValueNREC;
    public String device_mValueWBS;
    public String bluetooth_name;
    public String RSSI;

    public static int privacy = 2;
    public static int energyConsumption = 4;
    
    public BluetoothLog(int id, double timeStamp, long expId, String probeName, int profileId,
                        String appearance, String class_mClass, String device_mAddress, String device_mRemoteBrsf,
                        String device_mValueNREC, String device_mValueWBS, String bluetooth_name, String RSSI) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.appearance = appearance;
        this.class_mClass = class_mClass;
        this.device_mAddress = device_mAddress;
        this.device_mRemoteBrsf = device_mRemoteBrsf;
        this.device_mValueNREC = device_mValueNREC;
        this.device_mValueWBS = device_mValueWBS;
        this.bluetooth_name = bluetooth_name;
        this.RSSI = RSSI;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", appearance='" + appearance + '\'' +
                ", class_mClass='" + class_mClass + '\'' +
                ", device_mAddress='" + device_mAddress + '\'' +
                ", device_mRemoteBrsf='" + device_mRemoteBrsf + '\'' +
                ", device_mValueNREC='" + device_mValueNREC + '\'' +
                ", device_mValueWBS='" + device_mValueWBS + '\'' +
                ", bluetooth_name='" + bluetooth_name + '\'' +
                ", RSSI='" + RSSI;
    }
}
