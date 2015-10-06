package structure.log.positioning;

import structure.log.BasicLog;

import java.util.Arrays;

/**
 * Created by Ethan on 2015-08-28.
 */
public class WifiLog extends BasicLog {
    public String bssid;
    public String ssid;
    public String[] capabilities;
    public int distanceCm;
    public int distanceSdCm;
    public int frequency;
    public int level;
    public double tsf;

    public static int privacy = 2;
    public static int energyConsumption = 4;

    public WifiLog(int id, double timeStamp, long expId, String probeName, int profileId,
                   String bssid, String ssid, String capabilities, int distanceCm,
                   int distanceSdCm, int frequency, int level, double tsf){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;
        
        this.bssid = bssid;
        this.ssid = ssid;
        if(capabilities.length() > 0){
        	String stripped = capabilities.substring(1, capabilities.length()-1);        	
            this.capabilities = stripped.split("\\]\\[");
        }
        this.distanceCm = distanceCm;
        this.distanceSdCm = distanceSdCm;
        this.frequency = frequency;
        this.level = level;
        this.tsf = tsf;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", bssid='" + bssid + '\'' +
                ", ssid='" + ssid + '\'' +
                ", capabilities=" + Arrays.toString(capabilities) +
                ", distanceCm=" + distanceCm +
                ", distanceSdCm=" + distanceSdCm +
                ", frequency=" + frequency +
                ", level=" + level +
                ", tsf=" + tsf;
    }
}
