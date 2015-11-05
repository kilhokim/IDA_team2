package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import project_team2.util.Keys;
import project_team2.util.Converters;
import structure.log.BasicLog;
import structure.log.motion.AccelerometerLog;
import weka.core.Instances;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

/**
 * Created by kilho on 15. 11. 5.
 */
public class TestSensorDataSetGenerator extends SensorDataSetGenerator {

  public TestSensorDataSetGenerator() {
    tableNames = getTableNames();
  }

  public ArrayList<String> getTableNames() {
    ArrayList<String> tableNames = new ArrayList<String>();
    Field[] fields = this.getClass().getSuperclass().getDeclaredFields();
    for (Field field : fields) {
      if (
              !field.getName().equals("labelName") &&
              !field.getName().equals("tableNames") &&
              !field.getName().equals("savePath") &&
              !field.getName().equals("fileName") &&
              !field.getName().equals("extension") &&
              !field.getName().equals("batchProcess") &&
              !field.getName().equals("timeWindowSize") &&
              !field.getName().equals("deltaThresh") &&
              !field.getName().equals("limit")
              ) {
        try {
          if ((boolean) field.get(this)) tableNames.add(field.getName());
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }
    return tableNames;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////    Should be included!     //////////////////////////////////
  public HashMap<Integer, Feature> generateDataSet(int sourceIndex) {
    HashMap<Integer, Feature> users = new HashMap<Integer, Feature>();
    if (batchProcess) {
      ArrayList<Integer> profileIds = DBReader.readProfileIds(sourceIndex);
      for (int profileId : profileIds) {
        // FIXME:
//				if (profileId==3) {
//					break;
//				}
				if (users.size() > 2) {
					break;
				}
        Double tempUserLabel = DBReader.readLabel(labelName, profileId, sourceIndex);
        Feature tempFeature = generateFeature_batchProcess(tableNames, profileId, tempUserLabel, sourceIndex);
        users.put(profileId, tempFeature);
      }
    } else {
      HashMap<Integer, HashMap<String, ArrayList<BasicLog>>> wholeLogs = new HashMap<>();
      for (String tableName : tableNames) {
        HashMap<Integer, ArrayList<BasicLog>> eachUserLogs = DBReader.readLog(tableName, "", sourceIndex);
        for (int profileId : eachUserLogs.keySet()) {
          if (wholeLogs.containsKey(profileId)) {
            wholeLogs.get(profileId).put(tableName, eachUserLogs.get(profileId));
          } else {
            HashMap<String, ArrayList<BasicLog>> tempLogs = new HashMap<String, ArrayList<BasicLog>>();
            tempLogs.put(tableName, eachUserLogs.get(profileId));
            wholeLogs.put(profileId, tempLogs);
          }
        }
      }
      for (int profileId : wholeLogs.keySet()) {
        Double tempUserLabel = DBReader.readLabel(labelName, profileId, sourceIndex);
        users.put(profileId, generateFeature(wholeLogs.get(profileId), tempUserLabel));
      }
    }
    DBConn.close();
    return users;
  }

  public Feature generateFeature_batchProcess(ArrayList<String> tableNames, int profileId, Double label, int sourceIndex) {
    System.out.println("********************************************");
    System.out.println("profileId: " + profileId);
    SensorFeature feature = new SensorFeature();
    // FIXME:
//    feature.setLabel(Converters.weightToClassNum(label));
    feature.setLabel(String.valueOf(label));

    System.out.println("Starting batch processing...");
    // Iterate over table names (probes)
    for (String tableName : tableNames) {
      ArrayList<Integer> expIds = DBReader.readExpIds(tableName, profileId, sourceIndex);
      if (tableName.equals("AccelerometerSensorProbe")) {

        int numAccInstances = 0;
        int expIdSize;
        // Run query to calculate the total number of instances
        try {
          ResultSet rs = DBConn.execQuery("SELECT expId, COUNT(*) AS expIdSize" +
                  " FROM " + tableName +
                  " WHERE profile_id=" + profileId +
                  // FIXME:
                  " AND HOUR(FROM_UNIXTIME(time_stamp)) < 24" +
                  " AND HOUR(FROM_UNIXTIME(time_stamp)) > 7" +
                  " GROUP BY expId", sourceIndex);
          while (rs.next()) {
            expIdSize = rs.getInt("expIdSize");
            // We abandon the logs which don't fit in the single time window
            // FIXME:
						if (expIdSize < limit)
              numAccInstances += expIdSize / timeWindowSize;
						else
							numAccInstances += limit / timeWindowSize;
          }
        } catch (SQLException e) {
          e.printStackTrace();
        }
        System.out.println("numAccInstances: " + numAccInstances);

        feature.setNumAccInstances(numAccInstances);
        double[][] values = new double[numAccInstances][43];
        int valueIdx = 0;

        // First iteration - to compute feature values for each instance
        for (int exp = 0; exp < expIds.size(); exp++) {
          int expId = expIds.get(exp);
          ArrayList<BasicLog> tempChunkLogs =
                  DBReader.readLog_customized(tableName,
                          // FIXME:
                          "where profile_id = " + profileId + " and expId = " + expId
                                  + " AND HOUR(FROM_UNIXTIME(time_stamp)) < 24"
                                  + " AND HOUR(FROM_UNIXTIME(time_stamp)) > 7"
//                          , sourceIndex);
										      +	" LIMIT 0, " + limit, sourceIndex);
          expIdSize = tempChunkLogs.size();
          int indexTimeWin = 0;

          double avg_x = 0, avg_y = 0, avg_z = 0;
          double sum_x = 0, sum_y = 0, sum_z = 0;
          double sum_x_sq = 0, sum_y_sq = 0, sum_z_sq = 0;
          double sum_diff_x = 0, sum_diff_y = 0, sum_diff_z = 0, sum_acc = 0;
          double time_btwn_peaks_x = 0, time_btwn_peaks_y = 0, time_btwn_peaks_z = 0;
          double min_x = Double.MAX_VALUE,
                  min_y = Double.MAX_VALUE,
                  min_z = Double.MAX_VALUE;
          double max_x = -Double.MAX_VALUE,
                  max_y = -Double.MAX_VALUE,
                  max_z = -Double.MAX_VALUE;
          double[] x_logs = new double[timeWindowSize];
          double[] y_logs = new double[timeWindowSize];
          double[] z_logs = new double[timeWindowSize];
          double[] timeStamp = new double[timeWindowSize];

          // Iterate over instances in current expIds
          for (int i = 0; i < tempChunkLogs.size(); i++) {
            indexTimeWin++;
            expIdSize--;
            AccelerometerLog log = (AccelerometerLog) tempChunkLogs.get(i);
            x_logs[indexTimeWin-1] = log.x;
            y_logs[indexTimeWin-1] = log.y;
            z_logs[indexTimeWin-1] = log.z;
            timeStamp[indexTimeWin-1] = log.timeStamp;

            // Generate the new single instance when it fits in the time window:
            if (indexTimeWin == timeWindowSize) {
              // 1. Calculate the average and stdev (avg_*, std_*)

              // Binary representation for peaks
              int[] peakIndexList_x = new int[timeWindowSize];
              int[] peakIndexList_y = new int[timeWindowSize];
              int[] peakIndexList_z = new int[timeWindowSize];

              /**
               * FIRST ITERATION IN A WINDOW OF SIZE 50
               */
              for (int l = 0; l < timeWindowSize; l++) {
                sum_x += x_logs[l]; sum_y += y_logs[l]; sum_z += z_logs[l];
                sum_x_sq += x_logs[l]*x_logs[l];
                sum_y_sq += y_logs[l]*y_logs[l];
                sum_z_sq += z_logs[l]*z_logs[l];
                if (x_logs[l] < min_x) min_x = x_logs[l];
                if (y_logs[l] < min_y) min_y = y_logs[l];
                if (z_logs[l] < min_z) min_z = z_logs[l];
                // Find highest peak
                if (x_logs[l] > max_x) {
                  max_x = x_logs[l];
                  peakIndexList_x[l] = 1;
                }
                if (y_logs[l] > max_y) {
                  max_y = y_logs[l];
                  peakIndexList_y[l] = 1;
                }
                if (z_logs[l] > max_z) {
                  max_z = z_logs[l];
                  peakIndexList_z[l] = 1;
                }
              }
              avg_x = sum_x/timeWindowSize;
              avg_y = sum_y/timeWindowSize;
              avg_z = sum_z/timeWindowSize;

              values[valueIdx][0] = avg_x; // avg_x
              values[valueIdx][1] = avg_y; // avg_y
              values[valueIdx][2] = avg_z; // avg_z
              values[valueIdx][3] =
                      Math.sqrt(sum_x_sq/timeWindowSize - avg_x*avg_x); // std_x
              values[valueIdx][4] =
                      Math.sqrt(sum_y_sq/timeWindowSize - avg_y*avg_y); // std_y
              values[valueIdx][5] =
                      Math.sqrt(sum_z_sq/timeWindowSize - avg_z*avg_z); // std_z

              // 2. Calculate the average absolute deviation(avg_diff_*),
              //    average acceleration(avg_acc) and bin distributions(bin_dist_#_*)

              // Bin distributions for x, y, z
              double[] dist_x = new double[10];
              double interval_x = (max_x - min_x)/10;
              double[] dist_y = new double[10];
              double interval_y = (max_y - min_y)/10;
              double[] dist_z = new double[10];
              double interval_z = (max_z - min_z)/10;

              int delta = 1;
              int foundPeakNum_x = 1;
              int foundPeakNum_y = 1;
              int foundPeakNum_z = 1;
              /**
               * SECOND ITERATION IN A WINDOW OF SIZE 50
               */
              for (int l = 0; l < timeWindowSize; l++) {
                sum_diff_x += Math.abs(x_logs[l] - avg_x);
                sum_diff_y += Math.abs(y_logs[l] - avg_y);
                sum_diff_z += Math.abs(z_logs[l] - avg_z);
                sum_acc += Math.sqrt(x_logs[l]*x_logs[l] + y_logs[l]*y_logs[l] +
                        z_logs[l]*z_logs[l]);
                if (x_logs[l] > max_x - (max_x-min_x)*delta*deltaThresh) {
                  peakIndexList_x[l] = 1;
                  foundPeakNum_x++;
                }
                if (y_logs[l] > max_y - (max_y-min_y)*delta*deltaThresh) {
                  peakIndexList_y[l] = 1;
                  foundPeakNum_y++;
                }
                if (z_logs[l] > max_z - (max_z-min_z)*delta*deltaThresh) {
                  peakIndexList_z[l] = 1;
                  foundPeakNum_z++;
                }

                // Calculate bin distributions
                double log_x = x_logs[l] - min_x;
                if (interval_x*0 <= log_x && log_x < interval_x*1) dist_x[0] += 1;
                else if (interval_x*1 <= log_x && log_x < interval_x*2) dist_x[1] += 1;
                else if (interval_x*2 <= log_x && log_x < interval_x*3) dist_x[2] += 1;
                else if (interval_x*3 <= log_x && log_x < interval_x*4) dist_x[3] += 1;
                else if (interval_x*4 <= log_x && log_x < interval_x*5) dist_x[4] += 1;
                else if (interval_x*5 <= log_x && log_x < interval_x*6) dist_x[5] += 1;
                else if (interval_x*6 <= log_x && log_x < interval_x*7) dist_x[6] += 1;
                else if (interval_x*7 <= log_x && log_x < interval_x*8) dist_x[7] += 1;
                else if (interval_x*8 <= log_x && log_x < interval_x*9) dist_x[8] += 1;
                else dist_x[9] += 1;

                double log_y = y_logs[l] - min_y;
                if (interval_y*0 <= log_y && log_y < interval_y*1) dist_y[0] += 1;
                else if (interval_y*1 <= log_y && log_y < interval_y*2) dist_y[1] += 1;
                else if (interval_y*2 <= log_y && log_y < interval_y*3) dist_y[2] += 1;
                else if (interval_y*3 <= log_y && log_y < interval_y*4) dist_y[3] += 1;
                else if (interval_y*4 <= log_y && log_y < interval_y*5) dist_y[4] += 1;
                else if (interval_y*5 <= log_y && log_y < interval_y*6) dist_y[5] += 1;
                else if (interval_y*6 <= log_y && log_y < interval_y*7) dist_y[6] += 1;
                else if (interval_y*7 <= log_y && log_y < interval_y*8) dist_y[7] += 1;
                else if (interval_y*8 <= log_y && log_y < interval_y*9) dist_y[8] += 1;
                else dist_y[9] += 1;

                double log_z = z_logs[l] - min_z;
                if (interval_z*0 <= log_z && log_z < interval_z*1) dist_z[0] += 1;
                else if (interval_z*1 <= log_z && log_z < interval_z*2) dist_z[1] += 1;
                else if (interval_z*2 <= log_z && log_z < interval_z*3) dist_z[2] += 1;
                else if (interval_z*3 <= log_z && log_z < interval_z*4) dist_z[3] += 1;
                else if (interval_z*4 <= log_z && log_z < interval_z*5) dist_z[4] += 1;
                else if (interval_z*5 <= log_z && log_z < interval_z*6) dist_z[5] += 1;
                else if (interval_z*6 <= log_z && log_z < interval_z*7) dist_z[6] += 1;
                else if (interval_z*7 <= log_z && log_z < interval_z*8) dist_z[7] += 1;
                else if (interval_z*8 <= log_z && log_z < interval_z*9) dist_z[8] += 1;
                else dist_z[9] += 1;
                //
                for (int d = 0; d < 10; d++) {
                  dist_x[d] /= timeWindowSize;
                  dist_y[d] /= timeWindowSize;
                  dist_z[d] /= timeWindowSize;
                }
              }
              values[valueIdx][6] = sum_diff_x/timeWindowSize; // avg_diff_x
              values[valueIdx][7] = sum_diff_y/timeWindowSize; // avg_diff_y
              values[valueIdx][8] = sum_diff_z/timeWindowSize; // avg_diff_z
              values[valueIdx][9] = sum_acc/timeWindowSize; // avg_acc

              /**
               * ITERATION FOR SEARCHING AT LEAST THREE PEAKS
               */
              // Loop for searching at least three peaks
              while (foundPeakNum_x < 3 || foundPeakNum_y < 3 || foundPeakNum_z < 3 ) {
                delta++;
//								System.out.println("max_x: "+ max_x+ " " +foundPeakNum_x + " " + foundPeakNum_y + " " + foundPeakNum_z);
                for (int l = 0; l < timeWindowSize; l++) {
                  if (x_logs[l] > max_x - (max_x-min_x)*delta*deltaThresh && foundPeakNum_x < 3) {
                    peakIndexList_x[l] = 1;
                    foundPeakNum_x++;
                  }
                  if (y_logs[l] > max_y - (max_y-min_y)*delta*deltaThresh && foundPeakNum_y < 3) {
                    peakIndexList_y[l] = 1;
                    foundPeakNum_y++;
                  }
                  if (z_logs[l] > max_z - (max_z-min_z)*delta*deltaThresh && foundPeakNum_z < 3) {
                    peakIndexList_z[l] = 1;
                    foundPeakNum_z++;
                  }
                }
                //								System.out.println("delta " + delta + " x: "+ foundPeakNum_x + " y: " + foundPeakNum_y + " z: "+ foundPeakNum_z);
              }

              // Calculate an average of the time between successive peaks
              /**
               * ?TH ITERATION IN A WINDOW OF SIZE 50
               */
              int indexFor_x = -1, indexFor_y =-1, indexFor_z =-1;
              for (int l = 0; l < timeWindowSize; l++) {
                if (peakIndexList_x[l] == 1) {
                  if (indexFor_x == -1) indexFor_x = l;
                  else {
                    time_btwn_peaks_x += timeStamp[l] - timeStamp[indexFor_x];
                    indexFor_x = l;
                  }
                }
                if(peakIndexList_y[l] == 1) {
                  if (indexFor_y == -1) indexFor_y = l;
                  else {
                    time_btwn_peaks_y += timeStamp[l] - timeStamp[indexFor_y];
                    indexFor_y = l;
                  }
                }
                if (peakIndexList_z[l] == 1) {
                  if (indexFor_z == -1) indexFor_z = l;
                  else {
                    time_btwn_peaks_z += timeStamp[l] - timeStamp[indexFor_z];
                    indexFor_z = l;
                  }
                }
              }
              values[valueIdx][10] = time_btwn_peaks_x / (foundPeakNum_x-1);
              values[valueIdx][11] = time_btwn_peaks_y / (foundPeakNum_y-1);
              values[valueIdx][12] = time_btwn_peaks_z / (foundPeakNum_z-1);

              for (int j = 13; j < 13+10; j++)
                values[valueIdx][j] = dist_x[j-13];
              for (int j = 23; j < 23+10; j++)
                values[valueIdx][j] = dist_y[j-23];
              for (int j = 33; j < 33+10; j++)
                values[valueIdx][j] = dist_z[j-33];


              if (valueIdx % 100 == 0) {
                System.out.println("valueIdx=" + valueIdx);
              }
              valueIdx++; // IMPORTANT: increase the valueIdx
              // after putting a single instance
              // Re-initialize the variables
              avg_x = 0; avg_y = 0; avg_z = 0;
              sum_x = 0; sum_y = 0; sum_z = 0;
              sum_x_sq = 0; sum_y_sq = 0; sum_z_sq = 0;
              sum_diff_x = 0; sum_diff_y = 0; sum_diff_z = 0; sum_acc = 0;
              time_btwn_peaks_x = 0; time_btwn_peaks_y = 0; time_btwn_peaks_z = 0;
              min_x = Double.MAX_VALUE;
              min_y = Double.MAX_VALUE;
              min_z = Double.MAX_VALUE;
              max_x = -Double.MAX_VALUE;
              max_y = -Double.MAX_VALUE;
              max_z = -Double.MAX_VALUE;
              x_logs = new double[timeWindowSize];
              y_logs = new double[timeWindowSize];
              z_logs = new double[timeWindowSize];
              timeStamp = new double[timeWindowSize];
              indexTimeWin = 0;

              if (expIdSize  < timeWindowSize) break;
            }
          }
        }

        // Print first 10 instances as samples
        for (int inst = 0; inst < 3; inst++) {
          for (int attr = 0; attr < values[inst].length; attr++) {
            System.out.print(values[inst][attr] + " ");
          }
          System.out.print("\n");
        }
        feature.setValues_Accelerometer(tableName, values);
      }
    }

    return feature;
  }

  public Instances transformToInstances(HashMap<Integer, Feature> dataSet) {
    Iterator<Feature> iterator = dataSet.values().iterator();

    Feature sampleFeature = iterator.next();

    Field[] numericFields = sampleFeature.getNumericAttributes();
    Field[] nominalFields = sampleFeature.getNominalAttributes();

    ArrayList<Attribute> attrList = new ArrayList<Attribute>();
    for (Field numericField : numericFields) attrList.add(new Attribute(numericField.getName()));
    for (Field nominalField : nominalFields) {
      ArrayList<String> labels = new ArrayList<String>();
      HashSet<String> uniqueLabels = new HashSet<>();
      for (int profileId : dataSet.keySet()) {
        try {
          uniqueLabels.add("" + nominalField.get(dataSet.get(profileId)));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      for (String uniqueLabel : uniqueLabels) labels.add(uniqueLabel);
      attrList.add(new Attribute(nominalField.getName(), labels));
    }

    // insts[profileId * valueIdx]
    Instances insts = new Instances("example_dataSet", attrList, 0);
    for (int profileId : dataSet.keySet()) {
      SensorFeature currFeature = (SensorFeature)dataSet.get(profileId);
      // Choose the minimum value of numExps from different kinds of sensors
      int numInstances = currFeature.numAccInstances;
      for (int inst = 0; inst < numInstances; inst++) {
        double[] instValues = new double[insts.numAttributes()];
        for (int i = 0; i < instValues.length; i++) {
          try {
            if (i < numericFields.length){
//							if (i == numericFields.length-1) instValues[i] = (double) numericFields[i].getDouble(dataSet.get(profileId));
//							else
              instValues[i] = ((double[]) numericFields[i].get(dataSet.get(profileId)))[inst];
            }
            else {
              String tempLabel = "" + nominalFields[i - numericFields.length].get(dataSet.get(profileId));
                // FIXME: 'labels' is the only nominal Field
                instValues[i] = Double.parseDouble(tempLabel);
//							instValues[i] = Converters.classNumToWeight(tempLabel);
//              instValues[i] = insts.attribute(i).indexOfValue(tempLabel);
            }
          } catch (IllegalAccessException e) {
            e.printStackTrace();
          }
        }
        insts.add(new DenseInstance(1.0, instValues));
      }
    }
    return insts;
  }

}
