package project_team2;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Ethan on 2015-09-24.
 */
public class SensorFeature implements Feature {

  // Total number of Accelerometer expIds
  public int numAccExps;
  // Total number of Gyroscope expIds
  public int numGyroExps;
  // Total number of RotationVector expIds
  public int numRotExps;

  // TODO:
  // AccelerometerSensorProbe
  public double[] avgStd;
  public double[] maxStd;
  public double[] minStd;

  // Gyroscope
  public double[] avgStdX;
  public double[] maxStdX;
  public double[] minStdX;
  public double[] avgStdY;
  public double[] maxStdY;
  public double[] minStdY;
  public double[] avgStdZ;
  public double[] maxStdZ;
  public double[] minStdZ;

  // RotationVector
  public double[] avgStdC;
  public double[] maxStdC;
  public double[] minStdC;
  public double[] rAvgStdX;
  public double[] rMaxStdX;
  public double[] rMinStdX;
  public double[] rAvgStdY;
  public double[] rMaxStdY;
  public double[] rMinStdY;
  public double[] rAvgStdZ;
  public double[] rMaxStdZ;
  public double[] rMinStdZ;

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

  // valueLists[exp][attrIdx]
  public void setValues_Accelerometer(String dataType, double[][] valueLists){
    if(dataType.equals("AccelerometerSensorProbe")){
      assert (valueLists[0].length == 3);
      numAccExps = valueLists.length;

      for (int exp = 0; exp < numAccExps; exp++) {
        avgStd[exp] = valueLists[exp][0];
        maxStd[exp] = valueLists[exp][1];
        minStd[exp] = valueLists[exp][2];
      }
    }
  }

  // valueLists[exp][attrIdx]
  public void setValues_Gyroscope(String dataType, double[][] valueLists){
    if(dataType.equals("GyroscopeSensorProbe")){
      assert (valueLists[0].length == 9);
      numGyroExps = valueLists.length;

      for (int exp = 0; exp < numAccExps; exp++) {
        avgStdX[exp] = valueLists[exp][0];
        maxStdX[exp] = valueLists[exp][1];
        minStdX[exp] = valueLists[exp][2];
        avgStdY[exp] = valueLists[exp][3];
        maxStdY[exp] = valueLists[exp][4];
        minStdY[exp] = valueLists[exp][5];
        avgStdZ[exp] = valueLists[exp][6];
        maxStdZ[exp] = valueLists[exp][7];
        minStdZ[exp] = valueLists[exp][8];
      }
    }
  }

  // valueLists[exp][attrIdx]
  public void setValues_RotationVector(String dataType, double[][] valueLists){
    if(dataType.equals("RotationVectorSensorProbe")){
      assert (valueLists[0].length == 12);
      numRotExps = valueLists.length;
      for (int exp = 0; exp < numRotExps; exp++) {
        avgStdC[exp] = valueLists[exp][0];
        maxStdC[exp] = valueLists[exp][1];
        minStdC[exp] = valueLists[exp][2];
        rAvgStdX[exp] = valueLists[exp][3];
        rMaxStdX[exp] = valueLists[exp][4];
        rMinStdX[exp] = valueLists[exp][5];
        rAvgStdY[exp] = valueLists[exp][6];
        rMaxStdY[exp] = valueLists[exp][7];
        rMinStdY[exp] = valueLists[exp][8];
        rAvgStdZ[exp] = valueLists[exp][9];
        rMaxStdZ[exp] = valueLists[exp][10];
        rMinStdZ[exp] = valueLists[exp][11];
      }
    }
  }

  public double getValue(int index) throws Exception {
    double result;
    Class<?> cls = this.getClass();
    Field attr = cls.getDeclaredField(numericAtts[index]);
    result = (double) attr.get(this);
    return result;
  }

  public void setValue (int index, double value) throws Exception {
    Class<?> cls = this.getClass();
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