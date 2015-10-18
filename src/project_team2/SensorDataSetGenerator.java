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
public class SensorDataSetGenerator {

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
  private boolean GyroscopeSensorProbe = true;
  private boolean OrientationSensorProbe = false;
  private boolean RotationVectorSensorProbe = true;

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
  static int timeWindowSize = 50;

  // for normalization - static variables are needed to conduct bactch process
  // Accelerometer
  static double maxAccNotSQRT = 0.0;

  // Gyroscope
  static double maxGyX = 0.0;
  static double maxGyY = 0.0;
  static double maxGyZ = 0.0;

  // RotationVector
  static double maxCos = 0.0;
  static double maxSinX = 0.0;
  static double maxSinY = 0.0;
  static double maxSinZ = 0.0;


  public SensorDataSetGenerator() {
    tableNames = getTableNames();
  }

  private ArrayList<String> getTableNames() {
    ArrayList<String> tableNames = new ArrayList<String>();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (!field.getName().equals("labelName") &&
              !field.getName().equals("tableNames") &&
              !field.getName().equals("savePath") &&
              !field.getName().equals("fileName") &&
              !field.getName().equals("extension") &&
              !field.getName().equals("batchProcess") &&
              !field.getName().equals("timeWindowSize") &&
              !field.getName().equals("maxAccNotSQRT") &&
              !field.getName().equals("maxGyX") &&
              !field.getName().equals("maxGyY") &&
              !field.getName().equals("maxGyZ") &&
              !field.getName().equals("maxCos") &&
              !field.getName().equals("maxSinX") &&
              !field.getName().equals("maxSinY") &&
              !field.getName().equals("maxSinZ")
              )
      {
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
    HashMap<Integer, ArrayList<SensorFeature>> users = sensorDataSetGen.generateDataSet(false);
    Instances dataSet = sensorDataSetGen.transformToInstances(users);
    dataSet.setClassIndex(dataSet.numAttributes() - 1);
    ReadWriteInstances.writeFile(dataSet, sensorDataSetGen.savePath, sensorDataSetGen.fileName, sensorDataSetGen.extension);
    DBConn.close();
  }

  public Instances transformToInstances(HashMap<Integer, ArrayList<SensorFeature>> dataSet) {
    Iterator<ArrayList<SensorFeature>> iterator = dataSet.values().iterator();

    ArrayList<SensorFeature> sampleFeature = iterator.next();

    Field[] numericFields = sampleFeature.get(0).getNumericAttributes();
    Field[] nominalFields = sampleFeature.get(0).getNominalAttributes();

    ArrayList<Attribute> attrList = new ArrayList<Attribute>();
    for (Field numericField : numericFields) attrList.add(new Attribute(numericField.getName()));
    for (Field nominalField : nominalFields) {
      ArrayList<String> labels = new ArrayList<String>();
      HashSet<String> uniqueLabels = new HashSet<>();

      for (int profileId : dataSet.keySet()) {
        try {
          uniqueLabels.add("" + nominalField.get(dataSet.get(profileId).get(0)));
        } catch (IllegalAccessException e) {
          e.printStackTrace();
        }
      }
      for (String uniqueLabel : uniqueLabels) labels.add(uniqueLabel);
      attrList.add(new Attribute(nominalField.getName(), labels));
    }

    int numOfInstances = 0;
    for( int profileId: dataSet.keySet()){
      numOfInstances += dataSet.get(profileId).size();
    }

    Instances insts = new Instances("example_dataSet", attrList, numOfInstances);
    for (int profileId : dataSet.keySet()) {
      for( int j =0 ; j < dataSet.get(profileId).size(); j++){
        double[] instValues = new double[insts.numAttributes()];
        for (int i = 0; i < instValues.length; i++) {
          try {
            if (i < numericFields.length) instValues[i] = (double) numericFields[i].get(dataSet.get(profileId).get(j));
            else {
              String tempLabel = "" + nominalFields[i - numericFields.length].get(dataSet.get(profileId).get(j));
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
  public HashMap<Integer, ArrayList<SensorFeature>> generateDataSet(boolean test) {
    HashMap<Integer, ArrayList<SensorFeature>> users = new HashMap<Integer, ArrayList<SensorFeature>>();
    if (batchProcess) {
      boolean normailze = false;
      ArrayList<Integer> profileIds = DBReader.readProfileIds(test);
      for (int profileId : profileIds) {
        String tempUserLabel = DBReader.readLabel(labelName, profileId, test);
        ArrayList<SensorFeature>tempFeatureList = generateFeature_batchProcess(tableNames, profileId, tempUserLabel, test, normailze);
        users.put(profileId, tempFeatureList);
      }
      normailze = true;
      for (int profileId : profileIds) {
        String tempUserLabel = DBReader.readLabel(labelName, profileId, test);
        ArrayList<SensorFeature>tempFeatureList = generateFeature_batchProcess(tableNames, profileId, tempUserLabel, test, normailze);
        users.put(profileId, tempFeatureList);
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

  private ArrayList<SensorFeature> generateFeature(HashMap<String, ArrayList<BasicLog>> userLogs, String label) {
    ArrayList<SensorFeature> featureList = new ArrayList<SensorFeature>();
    SensorFeature feature = new SensorFeature();
    feature.setLabel(label);
    for (String tableName : userLogs.keySet()) {
      if (tableName.equals("AccelerometerSensorProbe")) {
        ArrayList<BasicLog> logs = userLogs.get(tableName);
        double avgX = 0;
        double avgY = 0;
        double avgZ = 0;
        for (int i = 0; i < logs.size(); i++) {
          AccelerometerLog log = (AccelerometerLog) logs.get(i);
          avgX += log.x;
          avgY += log.y;
          avgZ += log.z;
        }
        avgX /= logs.size();
        avgY /= logs.size();
        avgZ /= logs.size();
        double[] values = new double[]{avgX, avgY, avgZ};
        feature.setValues_Accelerometer(tableName, values);
      } else if (tableName.equals("")) {
        // insert codes..
      }
    }
    return featureList;
  }

  public ArrayList<SensorFeature> generateFeature_batchProcess(ArrayList<String> tableNames, int profileId, String label,boolean test, boolean normalize) {
    ArrayList<SensorFeature> finalFeatureList = new ArrayList<SensorFeature>();
    HashMap<Integer, SensorFeature> featureMap = new HashMap<Integer, SensorFeature>();
    for ( String tableName : tableNames) {
      ArrayList<Integer> expIds = DBReader.readExpIds(tableName, profileId, test);
      if (tableName.equals("AccelerometerSensorProbe")) {
        if(normalize == false){
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId , test);
            for (int i = 0; i < tempChunkLogs.size(); i++) {
              AccelerometerLog log = (AccelerometerLog) tempChunkLogs.get(i);
              double accel=  log.x * log.x + log.y* log.y + log.z * log.z;
              if(accel > maxAccNotSQRT) maxAccNotSQRT = accel;
            }
          }
        }
        else{
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId , test);
            int expIdSize = tempChunkLogs.size();

            if( expIdSize >= timeWindowSize){
              int indexTimeWin = 0;
              double avgAcc = 0.0;
              double avgSqAcc = 0.0;

              double avgVarAcc =0.0;
              double maxStd = 0.0;
              double minStd = Double.MAX_VALUE;

              int numOfTimeWindow = 0;
              for (int i = 0; i < tempChunkLogs.size(); i++) {
                indexTimeWin++;
                expIdSize --;
                AccelerometerLog log = (AccelerometerLog) tempChunkLogs.get(i);
                double currentSqAcc =(log.x * log.x + log.y* log.y + log.z * log.z)/maxAccNotSQRT;
                double currentAcc = Math.sqrt(currentSqAcc);
                avgAcc += currentAcc;
                avgSqAcc += currentSqAcc;
                if(indexTimeWin == timeWindowSize){
                  numOfTimeWindow++;
                  double currentStd = Math.sqrt(avgSqAcc/timeWindowSize - Math.pow(avgAcc/timeWindowSize, 2));
                  avgVarAcc += currentStd;
                  if(currentStd>maxStd) maxStd = currentStd;
                  if(minStd > currentStd) minStd = currentStd;
                  // initialize
                  indexTimeWin = 0;
                  avgAcc = 0.0;
                  avgSqAcc =0.0;
                  if(expIdSize  < timeWindowSize) break;
                }
              }
//							System.out.println(profileId+"\t"+ expId+"\t"+ avgVarAcc/numOfTimeWindow+"\t" + maxStd + "\t" + minStd);
              double[] values = new double[]{avgVarAcc/numOfTimeWindow, maxStd, minStd};
              if(featureMap.containsKey(expId)){
                SensorFeature feature = featureMap.get(expId);
                feature.setValues_Accelerometer(tableName, values);
                featureMap.put(expId, feature);
              }
              else{
                SensorFeature feature = new SensorFeature();
                feature.setLabel(label);
                feature.setValues_Accelerometer(tableName, values);
                featureMap.put(expId, feature);
              }
            }
          }
        }
      } else if (tableName.equals("GyroscopeSensorProbe")) {
        if(normalize == false){
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId , test);
            for (int i = 0; i < tempChunkLogs.size(); i++) {
              GyroscopeLog log = (GyroscopeLog) tempChunkLogs.get(i);
              if(log.x > maxGyX) maxGyX = log.x;
              if(log.x > maxGyY) maxGyY = log.y;
              if(log.x > maxGyZ) maxGyZ = log.z;
            }
          }
        }
        else{
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId , test);
            int expIdSize = tempChunkLogs.size();
            if( expIdSize >= timeWindowSize){
              int indexTimeWin = 0;
              // initialize variables used in timewindow
              double avgX = 0.0;
              double avgSqX = 0.0;
              double avgY = 0.0;
              double avgSqY = 0.0;
              double avgZ = 0.0;
              double avgSqZ = 0.0;

              // initialize variables used in expId
              double avgStdX =0.0;
              double maxStdX = 0.0;
              double minStdX = Double.MAX_VALUE;
              double avgStdY =0.0;
              double maxStdY = 0.0;
              double minStdY = Double.MAX_VALUE;
              double avgStdZ =0.0;
              double maxStdZ = 0.0;
              double minStdZ = Double.MAX_VALUE;

              int numOfTimeWindow = 0;
              for (int i = 0; i < tempChunkLogs.size(); i++) {
                indexTimeWin++;
                expIdSize --;
                GyroscopeLog log = (GyroscopeLog) tempChunkLogs.get(i);
                // calculate sum, squared sum values per each coordinate
                avgX += log.x/maxGyX ;
                avgSqX += Math.pow(log.x/maxGyX, 2);
                avgY += log.y/maxGyY ;
                avgSqY += Math.pow(log.y/maxGyY, 2);
                avgZ += log.z/maxGyZ ;
                avgSqZ += Math.pow(log.z/maxGyZ, 2);
                // calculate Std in timewindow

                if(indexTimeWin == timeWindowSize){
                  numOfTimeWindow++;
                  double currentStdX = Math.sqrt(avgSqX/timeWindowSize - Math.pow(avgX/timeWindowSize, 2));
                  avgStdX += currentStdX;
                  if(currentStdX>maxStdX) maxStdX = currentStdX;
                  if(minStdX > currentStdX) minStdX = currentStdX;
                  double currentStdY = Math.sqrt(avgSqY/timeWindowSize - Math.pow(avgY/timeWindowSize, 2));
                  avgStdY += currentStdY;
                  if(currentStdY>maxStdY) maxStdY = currentStdY;
                  if(minStdY > currentStdY) minStdY = currentStdY;
                  double currentStdZ = Math.sqrt(avgSqZ/timeWindowSize - Math.pow(avgZ/timeWindowSize, 2));
                  avgStdZ += currentStdZ;
                  if(currentStdZ>maxStdZ) maxStdZ = currentStdZ;
                  if(minStdZ > currentStdZ) minStdZ = currentStdZ;
                  // initialize
                  indexTimeWin = 0;
                  avgX = 0.0;
                  avgSqX =0.0;
                  avgY = 0.0;
                  avgSqY =0.0;
                  avgZ = 0.0;
                  avgSqZ =0.0;
                  if(expIdSize  < timeWindowSize) break;
                }
              }
//							System.out.println(profileId+"\t"+ expId+
//									"\t"+ avgStdX/numOfTimeWindow+"\t" + maxStdX + "\t" + minStdX +
//									"\t"+ avgStdY/numOfTimeWindow+"\t" + maxStdY + "\t" + minStdY +
//									"\t"+ avgStdZ/numOfTimeWindow+"\t" + maxStdZ + "\t" + minStdZ);
              double[] values = new double[]{avgStdX/numOfTimeWindow, maxStdX, minStdX,
                      avgStdY/numOfTimeWindow, maxStdY, minStdY,
                      avgStdZ/numOfTimeWindow, maxStdZ, minStdZ};
              if(featureMap.containsKey(expId)){
                SensorFeature feature = featureMap.get(expId);
                feature.setValues_Gyroscope(tableName, values);
                featureMap.put(expId, feature);
              }
              else{
                SensorFeature feature = new SensorFeature();
                feature.setLabel(label);
                feature.setValues_Gyroscope(tableName, values);
                featureMap.put(expId, feature);
              }
            }
          }
        }
      }
      else if(tableName.equals("RotationVectorSensorProbe")){
        if(normalize == false){
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId , test);
            for (int i = 0; i < tempChunkLogs.size(); i++) {
              RotationVectorLog log = (RotationVectorLog) tempChunkLogs.get(i);
              if(log.cosThetaOver2 > maxCos) maxCos = log.cosThetaOver2;
              if(log.xSinThetaOver2 > maxSinX) maxSinX = log.xSinThetaOver2;
              if(log.ySinThetaOver2 > maxSinY) maxSinY = log.ySinThetaOver2;
              if(log.zSinThetaOver2 > maxSinZ) maxSinZ = log.zSinThetaOver2;
            }
          }
        }
        else{
          for (int j = 0; j < expIds.size(); j++) {
            int expId = expIds.get(j);
            ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                    "where profile_id = " + profileId + " and expId = " + expId  , test);
            int expIdSize = tempChunkLogs.size();
            if( expIdSize >= timeWindowSize){
              int indexTimeWin = 0;
              // initialize variables used in timewindow
              double avgC = 0.0;
              double avgSqC = 0.0;
              double avgX = 0.0;
              double avgSqX = 0.0;
              double avgY = 0.0;
              double avgSqY = 0.0;
              double avgZ = 0.0;
              double avgSqZ = 0.0;

              // initialize variables used in expId
              double avgStdC =0.0;
              double maxStdC = 0.0;
              double minStdC = Double.MAX_VALUE;
              double avgStdX =0.0;
              double maxStdX = 0.0;
              double minStdX = Double.MAX_VALUE;
              double avgStdY =0.0;
              double maxStdY = 0.0;
              double minStdY = Double.MAX_VALUE;
              double avgStdZ =0.0;
              double maxStdZ = 0.0;
              double minStdZ = Double.MAX_VALUE;

              int numOfTimeWindow = 0;
              for (int i = 0; i < tempChunkLogs.size(); i++) {
                indexTimeWin++;
                expIdSize --;
                RotationVectorLog log = (RotationVectorLog) tempChunkLogs.get(i);

                // calculate sum, squared sum values per each coordinate
                avgC += log.cosThetaOver2/maxCos;
                avgSqC += Math.pow(log.cosThetaOver2/maxCos, 2);
                avgX += log.xSinThetaOver2/maxSinX ;
                avgSqX += Math.pow(log.xSinThetaOver2/maxSinX, 2);
                avgY += log.ySinThetaOver2/maxSinY  ;
                avgSqY += Math.pow(log.ySinThetaOver2/maxSinY, 2);
                avgZ += log.zSinThetaOver2/maxSinZ ;
                avgSqZ += Math.pow(log.zSinThetaOver2/maxSinZ, 2);

                // calculate Std in timewindow
                if(indexTimeWin == timeWindowSize){
                  numOfTimeWindow++;
                  double currentStdC = Math.sqrt(avgSqC/timeWindowSize - Math.pow(avgC/timeWindowSize, 2));
                  avgStdC += currentStdC;
                  if(currentStdC>maxStdC) maxStdC = currentStdC;
                  if(minStdC > currentStdC) minStdC = currentStdC;

                  double currentStdX = Math.sqrt(avgSqX/timeWindowSize - Math.pow(avgX/timeWindowSize, 2));
                  avgStdX += currentStdX;
                  if(currentStdX>maxStdX) maxStdX = currentStdX;
                  if(minStdX > currentStdX) minStdX = currentStdX;

                  double currentStdY = Math.sqrt(avgSqY/timeWindowSize - Math.pow(avgY/timeWindowSize, 2));
                  avgStdY += currentStdY;
                  if(currentStdY>maxStdY) maxStdY = currentStdY;
                  if(minStdY > currentStdY) minStdY = currentStdY;

                  double currentStdZ = Math.sqrt(avgSqZ/timeWindowSize - Math.pow(avgZ/timeWindowSize, 2));
                  avgStdZ += currentStdZ;
                  if(currentStdZ>maxStdZ) maxStdZ = currentStdZ;
                  if(minStdZ > currentStdZ) minStdZ = currentStdZ;

                  // initialize
                  indexTimeWin = 0;
                  avgC = 0.0;
                  avgSqC =0.0;
                  avgX = 0.0;
                  avgSqX =0.0;
                  avgY = 0.0;
                  avgSqY =0.0;
                  avgZ = 0.0;
                  avgSqZ =0.0;
                  if(expIdSize  < timeWindowSize) break;
                }
              }
//							System.out.println(profileId+"\t"+ expId+
//									"\t"+ avgStdC/numOfTimeWindow+"\t" + maxStdC + "\t" + minStdC +
//									"\t"+ avgStdX/numOfTimeWindow+"\t" + maxStdX + "\t" + minStdX +
//									"\t"+ avgStdY/numOfTimeWindow+"\t" + maxStdY + "\t" + minStdY +
//									"\t"+ avgStdZ/numOfTimeWindow+"\t" + maxStdZ + "\t" + minStdZ);
              double[] values = new double[]{avgStdC/numOfTimeWindow, maxStdC, minStdC,
                      avgStdX/numOfTimeWindow, maxStdX, minStdX,
                      avgStdY/numOfTimeWindow, maxStdY, minStdY,
                      avgStdZ/numOfTimeWindow, maxStdZ, minStdZ};
              if(featureMap.containsKey(expId)){
                SensorFeature feature = featureMap.get(expId);
                feature.setValues_RotationVector(tableName, values);
                featureMap.put(expId, feature);
              }
              else{
                SensorFeature feature = new SensorFeature();
                feature.setLabel(label);
                feature.setValues_RotationVector(tableName, values);
                featureMap.put(expId, feature);
              }
            }
          }
        }
      }
    }
    for(int expId: featureMap.keySet()){
      finalFeatureList.add(featureMap.get(expId));
    }
    return finalFeatureList;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////
}