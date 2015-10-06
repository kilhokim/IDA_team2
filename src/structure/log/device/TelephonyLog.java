package structure.log.device;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class TelephonyLog extends BasicLog {
    public int callState;
    public int deviceId;
    public String deviceSoftwareVersion;
    public int hassIccCard;
    public String line1Number;
    public String networkCountryIso;
    public String networkOperator;
    public String networkOperatorName;
    public int networkType;
    public int phoneType;
    public String simCountryIso;
    public String simOperator;
    public String simOperatorName;
    public double simSerialNumber;
    public int simState;
    public int subscriberId;
    public String voicemailAlphaTag;
    public String voicemailNumber;

    public static int privacy = 2;
    public static int energyConsumption = 1;

    public TelephonyLog(int id, double timeStamp, long expId, String probeName, int profileId,
                        int callState, double deviceId, String deviceSoftwareVersion,
                        int hassIccCard, String line1Number, String networkCountryIso,
                        String networkOperator, String networkOperatorName, int networkType,
                        int phoneType, String simCountryIso, String simOperator,
                        String simOperatorName, double simSerialNumber, int simState,
                        double subscriberId, String voicemailAlphaTag, String voicemailNumber) {
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 1814400;
        super.scheduleDuration = 0;

        this.callState = callState;
        this.deviceId = (int) deviceId;
        this.deviceSoftwareVersion = deviceSoftwareVersion;
        this.hassIccCard = hassIccCard;
        this.line1Number = line1Number;
        this.networkCountryIso = networkCountryIso;
        this.networkOperator = networkOperator;
        this.networkOperatorName = networkOperatorName;
        this.networkType = networkType;
        this.phoneType = phoneType;
        this.simCountryIso = simCountryIso;
        this.simOperator = simOperator;
        this.simOperatorName = simOperatorName;
        this.simSerialNumber = simSerialNumber;
        this.simState = simState;
        this.subscriberId = (int) subscriberId;
        this.voicemailAlphaTag = voicemailAlphaTag;
        this.voicemailNumber = voicemailNumber;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", callState=" + callState +
                ", deviceId=" + deviceId +
                ", deviceSoftwareVersion=" + deviceSoftwareVersion +
                ", hassIccCard=" + hassIccCard +
                ", line1Number=" + line1Number +
                ", networkCountryIso=" + networkCountryIso +
                ", networkOperator=" + networkOperator +
                ", networkOperatorName=" + networkOperatorName +
                ", networkType=" + networkType +
                ", phoneType=" + phoneType +
                ", simCountryIso=" + simCountryIso +
                ", simOperator=" + simOperator +
                ", simOperatorName=" + simOperatorName +
                ", simSerialNumber=" + simSerialNumber +
                ", simState=" + simState +
                ", subscriberId=" + subscriberId +
                ", voicemailAlphaTag=" + voicemailAlphaTag +
                ", voicemailNumber=" + voicemailNumber;
    }
}
