package structure.log.social;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class CallLog extends BasicLog {
    public int duration;
    public int number;
    public int numberType;
    public int type;

    public static int privacy = 5;
    public static int energyConsumption = 1;

    public CallLog(int id, double timeStamp, long expId, String probeName, int profileId,
                   int duration, double number, int numberType, int type){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.duration = duration;
        this.number = (int) number;
        this.numberType = numberType;
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", duration=" + duration +
                ", number=" + number +
                ", numberType=" + numberType +
                ", type=" + type;
    }
}
