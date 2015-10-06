package structure.log.positioning;

import structure.log.BasicLog;

import java.util.Arrays;

/**
 * Created by Ethan on 2015-08-28.
 */
public class CellTowerLog extends BasicLog {
    public int cid;
    public int lac;
    public double psc;
    public int type;

    public static int privacy = 2;
    public static int energyConsumption = 4;

    public CellTowerLog(int id, double timeStamp, long expId, String probeName, int profileId,
                        int cid, int lac, double psc, int type){
        super(id, timeStamp, expId, probeName, profileId);
        super.scheduleInterval = 0;
        super.scheduleDuration = 0;

        this.cid = cid;
        this.lac = lac;
        this.psc = psc;
        this.type = type;
    }

    @Override
    public String toString() {
        return super.toString() +
                ", cid=" + cid +
                ", lac=" + lac +
                ", psc=" + psc +
                ", type=" + type;
    }
}
