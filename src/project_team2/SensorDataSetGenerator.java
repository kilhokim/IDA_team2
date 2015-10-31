package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import operator.ReadWriteInstances;
import structure.log.BasicLog;
import structure.log.motion.AccelerometerLog;
import structure.log.motion.GyroscopeLog;
import structure.log.motion.RotationVectorLog;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;

/**
 * Created by Ethan on 2015-09-23.
 */
public class SensorDataSetGenerator implements DataSetGenerator {

  String labelName = "gender";
  ArrayList<String> tableNames;

  String savePath = "dataSet\\";
  String fileName = "AccelerometerSensorProbe";
  String extension = "arff";

  // the size of data processing at a time on memory
  boolean batchProcess = true;

  // assign true value to the variables below, if you want to use the corresponding data tables.

  // motion
  private boolean AccelerometerSensorProbe = true;
  private boolean GravitySensorProbe = false;
  private boolean LinearAccelerationSensorProbe = false;
  private boolean GyroscopeSensorProbe = false;
  private boolean OrientationSensorProbe = false;
  private boolean RotationVectorSensorProbe = false;

  // positioning
  private boolean BluetoothProbe = false;
  private boolean WifiProbe = false;
  private boolean CellTowerProbe = false;
  private boolean LocationProbe = false;
  private boolean SimpleLocationProbe = false;

  // environment
  private boolean MagneticFieldSensorProbe = false;
  private boolean ProximitySensorProbe = false;
  private boolean LightSensorProbe = false;
  private boolean PressureSensorProbe = false;
  private boolean AudioFeaturesProbe = false;

  // social
  private boolean CallLogProbe = false;
  private boolean SmsProbe = false;
  private boolean ContactProbe = false;

  // device interaction
  private boolean BrowserBookmarksProbe = false;
  private boolean BrowserSearchesProbe = false;
  private boolean ImageMediaProbe = false;
  private boolean VideoMediaProbe = false;
  private boolean AudioMediaProbe = false;
  private boolean RunningApplicationsProbe = false;
  private boolean ApplicationsProbe = false;
  private boolean ScreenProbe = false;
  private boolean AccountsProbe = false;
  private boolean ProcessStatisticsProbe = false;
  private boolean ServicesProbe = false;

  // device
  private boolean AndroidInfoProbe = false;
  private boolean BatteryProbe = false;
  private boolean HardwareInfoProbe = false;
  private boolean NetworkSettingsProbe = false;
  private boolean SystemSettingsProbe = false;
  private boolean TelephonyProbe = false;

  // time window size
  static int timeWindowSize = 500;

  public SensorDataSetGenerator() {
    tableNames = getTableNames();
  }

  public ArrayList<String> getTableNames() {
    ArrayList<String> tableNames = new ArrayList<String>();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (
        !field.getName().equals("labelName") &&
        !field.getName().equals("tableNames") &&
        !field.getName().equals("savePath") &&
        !field.getName().equals("fileName") &&
        !field.getName().equals("extension") &&
        !field.getName().equals("batchProcess") &&
        !field.getName().equals("timeWindowSize")
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

  public static void main(String[] args) {
    SensorDataSetGenerator sensorDataSetGen = new SensorDataSetGenerator();
    HashMap<Integer, Feature> users = sensorDataSetGen.generateDataSet(false);
//    Instances dataSet = sensorDataSetGen.transformToInstances(users);
//    dataSet.setClassIndex(dataSet.numAttributes() - 1);
//    ReadWriteInstances.writeFile(dataSet, sensorDataSetGen.savePath, sensorDataSetGen.fileName, sensorDataSetGen.extension);
    DBConn.close();
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

    // insts[profileId * exp]
    Instances insts = new Instances("example_dataSet", attrList, 0);
    for (int profileId : dataSet.keySet()) {
      SensorFeature currFeature = (SensorFeature)dataSet.get(profileId);
      // Choose the minimum value of numExps from different kinds of sensors
      int numInstances = currFeature.numAccInstances;
      for (int inst = 0; inst < numInstances; inst++) {
        double[] instValues = new double[insts.numAttributes()];
        for (int i = 0; i < instValues.length; i++) {
          try {
            if (i < numericFields.length)
              instValues[i] = ((double[])numericFields[i].get(dataSet.get(profileId)))[inst];
            else {
              String tempLabel = "" + nominalFields[i - numericFields.length].get(dataSet.get(profileId));
              instValues[i] = insts.attribute(i).indexOfValue(tempLabel);
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

  ////////////////////////////////////////////////////////////////////////////////////////////////
  //////////////////////////////////    Should be included!     //////////////////////////////////
  public HashMap<Integer, Feature> generateDataSet(boolean test) {
    HashMap<Integer, Feature> users = new HashMap<Integer, Feature>();
    if (batchProcess) {
      ArrayList<Integer> profileIds = DBReader.readProfileIds(test);
      for (int profileId : profileIds) {
        String tempUserLabel = DBReader.readLabel(labelName, profileId, test);
        Feature tempFeature = generateFeature_batchProcess(tableNames, profileId, tempUserLabel, test);
        users.put(profileId, tempFeature);
      }
    } else {
      HashMap<Integer, HashMap<String, ArrayList<BasicLog>>> wholeLogs = new HashMap<>();
      for (String tableName : tableNames) {
        HashMap<Integer, ArrayList<BasicLog>> eachUserLogs = DBReader.readLog(tableName, "", test);
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
        String tempUserLabel = DBReader.readLabel(labelName, profileId, test);
        users.put(profileId, generateFeature(wholeLogs.get(profileId), tempUserLabel));
      }
    }
    DBConn.close();
    return users;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////

  /**
   * @deprecated
   */
  public Feature generateFeature(HashMap<String, ArrayList<BasicLog>> userLogs, String label) {
    Feature feature = new SensorFeature();
    feature.setLabel(label);
    for (String tableName : userLogs.keySet()) {
      if (tableName.equals("AccelerometerSensorProbe")) {
//        ArrayList<BasicLog> logs = userLogs.get(tableName);
//        double avgX = 0;
//        double avgY = 0;
//        double avgZ = 0;
//        for (int i = 0; i < logs.size(); i++) {
//          AccelerometerLog log = (AccelerometerLog) logs.get(i);
//          avgX += log.x;
//          avgY += log.y;
//          avgZ += log.z;
//        }
//        avgX /= logs.size();
//        avgY /= logs.size();
//        avgZ /= logs.size();
//        double[] values = new double[]{avgX, avgY, avgZ};
//        feature.setValues_Accelerometer(tableName, values);
      } else if (tableName.equals("")) {
        // insert codes..
      }
    }
    return feature;
  }

  public Feature generateFeature_batchProcess(ArrayList<String> tableNames, int profileId, String label, boolean test) {
    SensorFeature feature = new SensorFeature();
    feature.setLabel(label);

    System.out.println("Starting batch processing...");
    // Iterate over table names (probes)
    for (String tableName : tableNames) {
      ArrayList<Integer> expIds = DBReader.readExpIds(tableName, profileId, test);
      if (tableName.equals("AccelerometerSensorProbe")) {

        int numAccInstances = 0;

        // First iteration - to calculate the total number of instances
        for (int exp = 0; exp < expIds.size(); exp++) {
          int expId = expIds.get(exp);
          ArrayList<BasicLog> tempChunkLogs =
            DBReader.readLog_customized(tableName,
            "where profile_id = " + profileId + " and expId = " + expId , test);
          int expIdSize = tempChunkLogs.size();

          // We abandon the logs which don't fit in the single time window
          numAccInstances += expIdSize / timeWindowSize;
        }

        feature.setNumAccInstances(numAccInstances);
        double[][] values = new double[numAccInstances][43];
        int valueIdx = 0;

        // Second iteration - to compute feature values for each instance
        for (int exp = 0; exp < expIds.size(); exp++) {
          int expId = expIds.get(exp);
          ArrayList<BasicLog> tempChunkLogs =
            DBReader.readLog_customized(tableName,
            "where profile_id = " + profileId + " and expId = " + expId , test);
          int expIdSize = tempChunkLogs.size();
          int indexTimeWin = 0;

          double avg_x = 0, avg_y = 0, avg_z = 0;
          double sum_x = 0, sum_y = 0, sum_z = 0;
          double sum_x_sq = 0, sum_y_sq = 0, sum_z_sq = 0;
          double sum_diff_x = 0, sum_diff_y = 0, sum_diff_z = 0, sum_acc = 0;
          double min_x = Double.MAX_VALUE,
                 min_y = Double.MAX_VALUE,
                 min_z = Double.MAX_VALUE;
          double max_x = Double.MIN_VALUE,
                 max_y = Double.MIN_VALUE,
                 max_z = Double.MIN_VALUE;
          double[] x_logs = new double[timeWindowSize];
          double[] y_logs = new double[timeWindowSize];
          double[] z_logs = new double[timeWindowSize];

          // Iterate over instances in current expIds
          for (int i = 0; i < tempChunkLogs.size(); i++) {
            indexTimeWin++;
            expIdSize--;
            AccelerometerLog log = (AccelerometerLog) tempChunkLogs.get(i);
            x_logs[indexTimeWin-1] = log.x;
            y_logs[indexTimeWin-1] = log.y;
            z_logs[indexTimeWin-1] = log.z;

            // Generate the new single instance when it fits in the time window:
            if (indexTimeWin == timeWindowSize) {
              // 1. Calculate the average and stdev (avg_*, std_*)
              for (int l = 0; l < timeWindowSize; l++) {
                sum_x += x_logs[l]; sum_y += y_logs[l]; sum_z += z_logs[l];
                sum_x_sq += x_logs[l]*x_logs[l];
                sum_y_sq += y_logs[l]*y_logs[l];
                sum_z_sq += z_logs[l]*z_logs[l];

                if (x_logs[l] < min_x) min_x = x_logs[l];
                if (x_logs[l] > max_x) max_x = x_logs[l];
                if (y_logs[l] < min_y) min_y = y_logs[l];
                if (y_logs[l] > max_y) max_y = y_logs[l];
                if (z_logs[l] < min_z) min_z = z_logs[l];
                if (z_logs[l] > max_z) max_z = z_logs[l];
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

              // 2. Calculate the average absolute deviation (avg_diff_*)
              //    and average acceleration (avg_acc)
              for (int l = 0; l < timeWindowSize; l++) {
                sum_diff_x += Math.abs(x_logs[l] - avg_x);
                sum_diff_y += Math.abs(y_logs[l] - avg_y);
                sum_diff_z += Math.abs(z_logs[l] - avg_z);
                sum_acc += x_logs[l]*x_logs[l] + y_logs[l]*y_logs[l] +
                           z_logs[l]*z_logs[l];

              }
              values[valueIdx][6] = sum_diff_x/timeWindowSize; // avg_diff_x
              values[valueIdx][7] = sum_diff_y/timeWindowSize; // avg_diff_y
              values[valueIdx][8] = sum_diff_z/timeWindowSize; // avg_diff_z
              values[valueIdx][9] = Math.sqrt(sum_acc)/timeWindowSize; // avg_acc

              // values[valueIdx][10] = ?  // TODO: time_btwn_peaks_x
              // values[valueIdx][11] = ?  // TODO: time_btwn_peaks_y
              // values[valueIdx][12] = ?  // TODO: time_btwn_peaks_z
               values[valueIdx][10] = 0;
               values[valueIdx][11] = 0;
               values[valueIdx][12] = 0;

              double[] dist_x = getBinDist(x_logs, min_x, max_x);
              double[] dist_y = getBinDist(y_logs, min_y, max_y);
              double[] dist_z = getBinDist(z_logs, min_z, max_z);
              for (int j = 13; j < 13+10; j++)
                values[valueIdx][j] = dist_x[j-13];
              for (int j = 23; j < 23+10; j++)
                values[valueIdx][j] = dist_y[j-23];
              for (int j = 33; j < 33+10; j++)
                values[valueIdx][j] = dist_z[j-33];


              valueIdx++; // IMPORTANT: increase the valueIdx
                          // after putting a single instance
              // Re-initialize the variables
              avg_x = 0; avg_y = 0; avg_z = 0;
              sum_x = 0; sum_y = 0; sum_z = 0;
              sum_x_sq = 0; sum_y_sq = 0; sum_z_sq = 0;
              sum_diff_x = 0; sum_diff_y = 0; sum_diff_z = 0; sum_acc = 0;
              min_x = Double.MAX_VALUE;
              min_y = Double.MAX_VALUE;
              min_z = Double.MAX_VALUE;
              max_x = Double.MIN_VALUE;
              max_y = Double.MIN_VALUE;
              max_z = Double.MIN_VALUE;
              x_logs = new double[timeWindowSize];
              y_logs = new double[timeWindowSize];
              z_logs = new double[timeWindowSize];
              indexTimeWin = 0;

              if(expIdSize  < timeWindowSize) break;
            }
          }
        }

        for (int inst = 0; inst < 10; inst++) {
          System.out.println(values[inst].toString());
        }
        feature.setValues_Accelerometer(tableName, values);
      }
    }

    return feature;
  }

  public static double[] getBinDist(double[] logs, double min, double max) {
    double[] dist = new double[10];
    double interval = (max - min) / 10;
    for (int l = 0; l < logs.length; l++) {
      double log = logs[l] - min;
      if (interval*0 <= log && log < interval*1) dist[0] += 1;
      else if (interval*1 <= log && log < interval*2) dist[1] += 1;
      else if (interval*2 <= log && log < interval*3) dist[2] += 1;
      else if (interval*3 <= log && log < interval*4) dist[3] += 1;
      else if (interval*4 <= log && log < interval*5) dist[4] += 1;
      else if (interval*5 <= log && log < interval*6) dist[5] += 1;
      else if (interval*6 <= log && log < interval*7) dist[6] += 1;
      else if (interval*7 <= log && log < interval*8) dist[7] += 1;
      else if (interval*8 <= log && log < interval*9) dist[8] += 1;
      else dist[9] += 1;
    }

    for (int i = 0; i < dist.length; i++) {
      dist[i] /= logs.length;
    }

    return dist;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////
}