package project_team2;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Created by Ethan on 2015-09-24.
 */
public class SensorFeature implements Feature {

	// Total number of Accelerometer instances
	public int numAccInstances;

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

	public Double label;

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
			"bin_dist_9_z", "bin_dist_10_z", "label"};
	String[] nominalAtts = {};   // label must be the last one!

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

	public void setNumAccInstances(int numAccInstances) {
		this.numAccInstances = numAccInstances;
	}

	// valueLists[inst][attrIdx]
	public void setValues_Accelerometer(String dataType, double[][] valueLists){
		if(dataType.equals("AccelerometerSensorProbe")){
			assert (valueLists[0].length == 43);
			numAccInstances = valueLists.length;
			avg_x = new double[numAccInstances];
			avg_y = new double[numAccInstances];
			avg_z = new double[numAccInstances];
			std_x = new double[numAccInstances];
			std_y = new double[numAccInstances];
			std_z = new double[numAccInstances];
			avg_diff_x = new double[numAccInstances];
			avg_diff_y = new double[numAccInstances];
			avg_diff_z = new double[numAccInstances];
			avg_acc = new double[numAccInstances];
			time_btwn_peaks_x = new double[numAccInstances];
			time_btwn_peaks_y = new double[numAccInstances];
			time_btwn_peaks_z = new double[numAccInstances];
			bin_dist_1_x = new double[numAccInstances];
			bin_dist_2_x = new double[numAccInstances];
			bin_dist_3_x = new double[numAccInstances];
			bin_dist_4_x = new double[numAccInstances];
			bin_dist_5_x = new double[numAccInstances];
			bin_dist_6_x = new double[numAccInstances];
			bin_dist_7_x = new double[numAccInstances];
			bin_dist_8_x = new double[numAccInstances];
			bin_dist_9_x = new double[numAccInstances];
			bin_dist_10_x = new double[numAccInstances];
			bin_dist_1_y = new double[numAccInstances];
			bin_dist_2_y = new double[numAccInstances];
			bin_dist_3_y = new double[numAccInstances];
			bin_dist_4_y = new double[numAccInstances];
			bin_dist_5_y = new double[numAccInstances];
			bin_dist_6_y = new double[numAccInstances];
			bin_dist_7_y = new double[numAccInstances];
			bin_dist_8_y = new double[numAccInstances];
			bin_dist_9_y = new double[numAccInstances];
			bin_dist_10_y = new double[numAccInstances];
			bin_dist_1_z = new double[numAccInstances];
			bin_dist_2_z = new double[numAccInstances];
			bin_dist_3_z = new double[numAccInstances];
			bin_dist_4_z = new double[numAccInstances];
			bin_dist_5_z = new double[numAccInstances];
			bin_dist_6_z = new double[numAccInstances];
			bin_dist_7_z = new double[numAccInstances];
			bin_dist_8_z = new double[numAccInstances];
			bin_dist_9_z = new double[numAccInstances];
			bin_dist_10_z = new double[numAccInstances];

			for (int inst = 0; inst < numAccInstances; inst++) {
				avg_x[inst] = valueLists[inst][0];
				avg_y[inst] = valueLists[inst][1];
				avg_z[inst] = valueLists[inst][2];
				std_x[inst] = valueLists[inst][3];
				std_y[inst] = valueLists[inst][4];
				std_z[inst] = valueLists[inst][5];
				avg_diff_x[inst] = valueLists[inst][6];
				avg_diff_y[inst] = valueLists[inst][7];
				avg_diff_z[inst] = valueLists[inst][8];
				avg_acc[inst] = valueLists[inst][9];
				time_btwn_peaks_x[inst] = valueLists[inst][10];
				time_btwn_peaks_y[inst] = valueLists[inst][11];
				time_btwn_peaks_z[inst] = valueLists[inst][12];
				bin_dist_1_x[inst] = valueLists[inst][13];
				bin_dist_2_x[inst] = valueLists[inst][14];
				bin_dist_3_x[inst] = valueLists[inst][15];
				bin_dist_4_x[inst] = valueLists[inst][16];
				bin_dist_5_x[inst] = valueLists[inst][17];
				bin_dist_6_x[inst] = valueLists[inst][18];
				bin_dist_7_x[inst] = valueLists[inst][19];
				bin_dist_8_x[inst] = valueLists[inst][20];
				bin_dist_9_x[inst] = valueLists[inst][21];
				bin_dist_10_x[inst] = valueLists[inst][22];
				bin_dist_1_y[inst] = valueLists[inst][23];
				bin_dist_2_y[inst] = valueLists[inst][24];
				bin_dist_3_y[inst] = valueLists[inst][25];
				bin_dist_4_y[inst] = valueLists[inst][26];
				bin_dist_5_y[inst] = valueLists[inst][27];
				bin_dist_6_y[inst] = valueLists[inst][28];
				bin_dist_7_y[inst] = valueLists[inst][29];
				bin_dist_8_y[inst] = valueLists[inst][30];
				bin_dist_9_y[inst] = valueLists[inst][31];
				bin_dist_10_y[inst] = valueLists[inst][32];
				bin_dist_1_z[inst] = valueLists[inst][33];
				bin_dist_2_z[inst] = valueLists[inst][34];
				bin_dist_3_z[inst] = valueLists[inst][35];
				bin_dist_4_z[inst] = valueLists[inst][36];
				bin_dist_5_z[inst] = valueLists[inst][37];
				bin_dist_6_z[inst] = valueLists[inst][38];
				bin_dist_7_z[inst] = valueLists[inst][39];
				bin_dist_8_z[inst] = valueLists[inst][40];
				bin_dist_9_z[inst] = valueLists[inst][41];
				bin_dist_10_z[inst] = valueLists[inst][42];
			}
		}
	}

	public void setLabel(Double label){
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