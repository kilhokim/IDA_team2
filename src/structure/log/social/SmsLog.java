package structure.log.social;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class SmsLog extends BasicLog {
    public int address;
    public int locked;
    public int protocol;
    public int read;
    public int replyPathPresent;
    public String serviceCenter;
    public int status;
    public int threadId;
    public int type;

    public static int privacy = 5;
    public static int energyConsumption = 1;

    public SmsLog(int id, double timeStamp, long expId, String probeName, int profileId,
                  String address, int locked, int protocol, int read,
                  int replyPathPresent, String serviceCenter, int status,
                  int threadId, int type){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 900;
        super.scheduleDuration = 0;

        this.address = Integer.parseInt(address);
        this.locked = locked;
        this.protocol = protocol;
        this.read = read;
        this.replyPathPresent = replyPathPresent;
        this.serviceCenter = serviceCenter;
        this.status = status;
        this.threadId = threadId;
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", address=" + address +
                ", locked=" + locked +
                ", protocol=" + protocol +
                ", read=" + read +
                ", replyPathPresent=" + replyPathPresent +
                ", serviceCenter=" + serviceCenter +
                ", status=" + status +
                ", threadId=" + threadId +
                ", type=" + type;
    }
}
