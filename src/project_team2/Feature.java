package project_team2;

import java.lang.reflect.Field;

/**
 * Created by Ethan on 2015-09-24.
 */
public class Feature {

    public double avgX;
    public double avgY;
    public double avgZ;

    public String label;

    String[] numericAtts = {"avgX", "avgY", "avgZ"};
    String[] nominalAtts = {"label"};   // label must be the last one!

    public Field[] getNumericAttributes(){
        Field[] fields = null;
        fields = new Field[numericAtts.length];
        for(int i=0; i<fields.length; i++){
            try {
                fields[i] = this.getClass().getField(numericAtts[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return fields;
    }

    public Field[] getNominalAttributes(){
        Field[] fields = null;
        fields = new Field[nominalAtts.length];
        for(int i=0; i<fields.length; i++){
            try {
                fields[i] = this.getClass().getField(nominalAtts[i]);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
        }
        return fields;
    }

    public void setValues_Accelerometer(String dataType, double[] values){
        if(dataType.equals("AccelerometerSensorProbe")){
            avgX = values[0];
            avgY = values[1];
            avgZ = values[2];
        }
    }

    public void setLabel(String label){
        this.label = label;
    }
}
