package project_team2;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Ethan on 2015-09-24.
 */
public class SensorFeature implements Feature {

  // Total number of Accelerometer expIds
  public int numAccExps;

  // TODO:
  // AccelerometerSensorProbe
  public double[] avg_x;
  public double[] avg_y;
  public double[] avg_z;

  public double[] std_x;
  public double[] std_y;
  public double[] std_z;

  public double[] avg_diff_x;
  public double[] avg_diff_y;
  public double[] avg_diff_z;

  public double[] avg_acc;

  public double[] time_btwn_peaks_x;
  public double[] time_btwn_peaks_y;
  public double[] time_btwn_peaks_z;

  public double[] bin_dist_1_x;
  public double[] bin_dist_2_x;
  public double[] bin_dist_3_x;
  public double[] bin_dist_4_x;
  public double[] bin_dist_5_x;
  public double[] bin_dist_6_x;
  public double[] bin_dist_7_x;
  public double[] bin_dist_8_x;
  public double[] bin_dist_9_x;
  public double[] bin_dist_10_x;

  public double[] bin_dist_1_y;
  public double[] bin_dist_2_y;
  public double[] bin_dist_3_y;
  public double[] bin_dist_4_y;
  public double[] bin_dist_5_y;
  public double[] bin_dist_6_y;
  public double[] bin_dist_7_y;
  public double[] bin_dist_8_y;
  public double[] bin_dist_9_y;
  public double[] bin_dist_10_y;

  public double[] bin_dist_1_z;
  public double[] bin_dist_2_z;
  public double[] bin_dist_3_z;
  public double[] bin_dist_4_z;
  public double[] bin_dist_5_z;
  public double[] bin_dist_6_z;
  public double[] bin_dist_7_z;
  public double[] bin_dist_8_z;
  public double[] bin_dist_9_z;
  public double[] bin_dist_10_z;

  public String label;

  String[] numericAtts = {
    "avg_x", "avg_y", "avg_z", "std_x", "std_y", "std_z",
    "avg_diff_x", "avg_diff_y", "avg_diff_z", "avg_acc",
    "time_btwn_peaks_x", "time_btwn_peaks_y", "time_btwn_peaks_z",
    "bin_dist_1_x", "bin_dist_2_x", "bin_dist_3_x", "bin_dist_4_x",
    "bin_dist_5_x", "bin_dist_6_x", "bin_dist_7_x", "bin_dist_8_x",
    "bin_dist_9_x", "bin_dist_10_x", "bin_dist_1_y", "bin_dist_2_y",
    "bin_dist_3_y", "bin_dist_4_y", "bin_dist_5_y", "bin_dist_6_y",
    "bin_dist_7_y", "bin_dist_8_y", "bin_dist_9_y", "bin_dist_10_y",
    "bin_dist_1_z", "bin_dist_2_z", "bin_dist_3_z", "bin_dist_4_z",
    "bin_dist_5_z", "bin_dist_6_z", "bin_dist_7_z", "bin_dist_8_z",
    "bin_dist_9_z", "bin_dist_10_z"};
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
      assert (valueLists[0].length == 43);
      numAccExps = valueLists.length;

      for (int exp = 0; exp < numAccExps; exp++) {
        avg_x[exp] = valueLists[exp][0];
        avg_y[exp] = valueLists[exp][1];
        avg_z[exp] = valueLists[exp][2];
        std_x[exp] = valueLists[exp][3];
        std_y[exp] = valueLists[exp][4];
        std_z[exp] = valueLists[exp][5];
        avg_diff_x[exp] = valueLists[exp][6];
        avg_diff_y[exp] = valueLists[exp][7];
        avg_diff_z[exp] = valueLists[exp][8];
        avg_acc[exp] = valueLists[exp][9];
        time_btwn_peaks_x[exp] = valueLists[exp][10];
        time_btwn_peaks_y[exp] = valueLists[exp][11];
        time_btwn_peaks_z[exp] = valueLists[exp][12];
        bin_dist_1_x[exp] = valueLists[exp][13];
        bin_dist_2_x[exp] = valueLists[exp][14];
        bin_dist_3_x[exp] = valueLists[exp][15];
        bin_dist_4_x[exp] = valueLists[exp][16];
        bin_dist_5_x[exp] = valueLists[exp][17];
        bin_dist_6_x[exp] = valueLists[exp][18];
        bin_dist_7_x[exp] = valueLists[exp][19];
        bin_dist_8_x[exp] = valueLists[exp][20];
        bin_dist_9_x[exp] = valueLists[exp][21];
        bin_dist_10_x[exp] = valueLists[exp][22];
        bin_dist_1_y[exp] = valueLists[exp][23];
        bin_dist_2_y[exp] = valueLists[exp][24];
        bin_dist_3_y[exp] = valueLists[exp][25];
        bin_dist_4_y[exp] = valueLists[exp][26];
        bin_dist_5_y[exp] = valueLists[exp][27];
        bin_dist_6_y[exp] = valueLists[exp][28];
        bin_dist_7_y[exp] = valueLists[exp][29];
        bin_dist_8_y[exp] = valueLists[exp][30];
        bin_dist_9_y[exp] = valueLists[exp][31];
        bin_dist_10_y[exp] = valueLists[exp][32];
        bin_dist_1_z[exp] = valueLists[exp][33];
        bin_dist_2_z[exp] = valueLists[exp][34];
        bin_dist_3_z[exp] = valueLists[exp][35];
        bin_dist_4_z[exp] = valueLists[exp][36];
        bin_dist_5_z[exp] = valueLists[exp][37];
        bin_dist_6_z[exp] = valueLists[exp][38];
        bin_dist_7_z[exp] = valueLists[exp][39];
        bin_dist_8_z[exp] = valueLists[exp][40];
        bin_dist_9_z[exp] = valueLists[exp][41];
        bin_dist_10_z[exp] = valueLists[exp][42];
      }
    }
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