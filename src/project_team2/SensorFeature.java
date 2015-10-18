package project_team2;

import java.lang.reflect.Field;

/**
 * Created by Ethan on 2015-09-24.
 */
public class SensorFeature {

  // TODO:
  // AccelerometerSensorProbe
  public double avgStd;
  public double maxStd;
  public double minStd;

  // Gyroscope
  public double avgStdX;
  public double maxStdX;
  public double minStdX;
  public double avgStdY;
  public double maxStdY;
  public double minStdY;
  public double avgStdZ;
  public double maxStdZ;
  public double minStdZ;

  // RotationVector
  public double avgStdC;
  public double maxStdC;
  public double minStdC;
  public double rAvgStdX;
  public double rMaxStdX;
  public double rMinStdX;
  public double rAvgStdY;
  public double rMaxStdY;
  public double rMinStdY;
  public double rAvgStdZ;
  public double rMaxStdZ;
  public double rMinStdZ;

  public String label;

  String[] numericAtts = {"avgStd", "maxStd", "minStd"
          , "avgStdX", "maxStdX", "minStdX"
          , "avgStdY", "maxStdY", "minStdY"
          , "avgStdZ", "maxStdZ", "minStdZ"
          , "avgStdC", "maxStdC", "minStdC"
          , "rAvgStdX", "rMaxStdX", "rMinStdX"
          , "rAvgStdY", "rMaxStdY", "rMinStdY"
          , "rAvgStdZ", "rMaxStdZ", "rMinStdZ"};
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
      avgStd = values[0];
      maxStd = values[1];
      minStd = values[2];
    }
  }

  public void setValues_Gyroscope(String dataType, double[] values){
    if(dataType.equals("GyroscopeSensorProbe")){
      avgStdX = values[0];
      maxStdX = values[1];
      minStdX = values[2];
      avgStdY = values[3];
      maxStdY = values[4];
      minStdY = values[5];
      avgStdZ = values[6];
      maxStdZ = values[7];
      minStdZ = values[8];
    }
  }

  public void setValues_RotationVector(String dataType, double[] values){
    if(dataType.equals("RotationVectorSensorProbe")){
      // Fix !!!
      avgStdC = values[0];
      maxStdC = values[1];
      minStdC = values[2];
      rAvgStdX = values[3];
      rMaxStdX = values[4];
      rMinStdX = values[5];
      rAvgStdY = values[6];
      rMaxStdY = values[7];
      rMinStdY = values[8];
      rAvgStdZ = values[9];
      rMaxStdZ = values[10];
      rMinStdZ = values[11];
    }
  }

  public double getValue(int index) throws Exception, SecurityException{
    double result;
    Class<?> cls =this.getClass();
    Field attr = cls.getDeclaredField(numericAtts[index]);
    result = (double) attr.get(this);
    return result;
  }

  public void setValue(int index, double value) throws Exception, SecurityException{
    Class<?> cls =this.getClass();
    Field attr = cls.getDeclaredField(numericAtts[index]);
    attr.setDouble(this, value);
  }

  public void setLabel(String label){
    this.label = label;
  }

//    public static void main(String[] args) throws Exception, Exception {
//    	Feature tempFe = new Feature ();
//    	tempFe.avgStd = 1;
//    	tempFe.maxStd = 2;
//    	System.out.println(tempFe.getValue(0));
//    	tempFe.setValue(0, 10);
//    	System.out.println(tempFe.getValue(0));
//    }
}