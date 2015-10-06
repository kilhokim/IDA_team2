package structure.log.social;

import structure.log.BasicLog;

/**
 * Created by Ethan on 2015-08-28.
 */
public class ContactLog extends BasicLog {
    public int number;

    public static int privacy = 5;
    public static int energyConsumption = 1;

    public ContactLog(int id, double timeStamp, long expId, String probeName, int profileId,
                      String number){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 604800;
        super.scheduleDuration = 0;
        
        try {
        	this.number = Integer.parseInt(number);
        } catch (NumberFormatException e) {
        	
        }
    }

    @Override
    public String toString() {
        return super.toString() +
                ", number=" + number;
    }
}
