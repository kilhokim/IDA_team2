package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import operator.ReadWriteInstances;
import project_team2.util.Parser;
import structure.log.BasicLog;
import structure.log.deviceInteraction.ApplicationsLog;
import structure.log.deviceInteraction.AudioMediaLog;
import structure.log.deviceInteraction.ImageMediaLog;
import structure.log.deviceInteraction.VideoMediaLog;
import structure.log.motion.AccelerometerLog;
import structure.log.social.CallLog;
import structure.log.social.SmsLog;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by Ethan on 2015-09-23.
 */
public class NormalDataSetGenerator implements DataSetGenerator {

  String labelName = "gender";
  ArrayList<String> tableNames;

  String savePath = "dataSet\\";
  String fileName = "AccelerometerSensorProbe";
  String extension = "arff";

  // the size of data processing at a time on memory
  boolean batchProcess = true;

  // assign true value to the variables below, if you want to use the corresponding data tables.

  // motion
  private boolean AccelerometerSensorProbe = false;
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
  private boolean CallLogProbe = true;
  private boolean SmsProbe = true;
  private boolean ContactProbe = false;

  // device interaction
  private boolean BrowserBookmarksProbe = false;
  private boolean BrowserSearchesProbe = false;
  private boolean ImageMediaProbe = true;
  private boolean VideoMediaProbe = true;
  private boolean AudioMediaProbe = true;
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

  public Map<String, List<Integer>> nullFeatureProfileIdMap =
          new HashMap<String, List<Integer>>();


  public NormalDataSetGenerator() {
    tableNames = getTableNames();
  }

  public ArrayList<String> getTableNames() {
    ArrayList<String> tableNames = new ArrayList<String>();
    Field[] fields = this.getClass().getDeclaredFields();
    for (Field field : fields) {
      if (!field.getName().equals("labelName") &&
              !field.getName().equals("tableNames") &&
              !field.getName().equals("savePath") &&
              !field.getName().equals("fileName") &&
              !field.getName().equals("extension") &&
              !field.getName().equals("batchProcess") &&
              !field.getName().equals("nullFeatureProfileIdMap")) {
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
    NormalDataSetGenerator dataSetGen = new NormalDataSetGenerator();
    HashMap<Integer, Feature> users = dataSetGen.generateDataSet(false);
    Instances dataSet = dataSetGen.transformToInstances(users);
    dataSet.setClassIndex(dataSet.numAttributes() - 1);
    ReadWriteInstances.writeFile(dataSet, dataSetGen.savePath, dataSetGen.fileName, dataSetGen.extension);
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

    Instances insts = new Instances("example_dataSet", attrList, 0);
    for (int profileId : dataSet.keySet()) {
      double[] instValues = new double[insts.numAttributes()];
      for (int i = 0; i < instValues.length; i++) {
        try {
          if (i < numericFields.length) instValues[i] = (double) numericFields[i].get(dataSet.get(profileId));
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

      // IMPORTANT: Fill missing features for nullProfileIds
      users = fillMissingFeatures(users, nullFeatureProfileIdMap, profileIds, test);

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

    for (int i : users.keySet()) {
      System.out.println(i + ": " + users.get(i).toString());
    }
    return users;
  }
  ////////////////////////////////////////////////////////////////////////////////////////////////

  public Feature generateFeature(HashMap<String, ArrayList<BasicLog>> userLogs, String label) {
    Feature feature = new NormalFeature();
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
    NormalFeature feature = new NormalFeature();
    feature.setLabel(label);
    System.out.println("profileId=" + profileId + ", feature=" + feature.numSmss);

    TableNameLoop:
    for (String tableName : tableNames) {
      ArrayList<Integer> expIds = DBReader.readExpIds(tableName, profileId, test);

      /**
       @author Kilho Kim
       @description ApplicationsProbe features
       */
      if (tableName.equals("ApplicationsProbe")) {
        String currCategory;
        int[] values = new int[NormalFeature.categoryNames.length];

        final String GOOGLE_PLAY_URL =
                "https://play.google.com/store/apps/details?hl=en&id=";
        final String CATEGORY_CSS_QUERY = "span[itemprop=\"genre\"]";

        for (int j = 0; j < expIds.size(); j++) {
          int expId = expIds.get(j);
          ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                  "where profile_id = " + profileId + " and expId = " + expId, test);
          for (int i = 0; i < tempChunkLogs.size(); i++) {
            try {
              ApplicationsLog log =
                      (ApplicationsLog) tempChunkLogs.get(i);

              //                          currCategory = Parser.parseCategory(GOOGLE_PLAY_URL,
              //                                  log.packageName, CATEGORY_CSS_QUERY);
              currCategory = log.appCategory;
              if (currCategory == null) {
                currCategory = Parser.parseCategory(GOOGLE_PLAY_URL,
                        log.packageName, CATEGORY_CSS_QUERY);
              }
              int categoryIdx =
                      NormalFeature.categoryMap.get(currCategory);
              values[categoryIdx] += 1;
            } catch (IOException e) {
              continue;
              //                e.printStackTrace();
            } catch (NullPointerException e) {
              System.err.print("Not Found");
              continue;
            } catch (SQLException e) {
              System.err.println("SQLException: " + e);
            }
          }
        }
        printFeatureValue(values);
        feature.setValues_Applications(tableName, values);
      }
      /**
       @author Kilho Kim
       @description ImageMediaProbe features
       */
      else if (tableName.equals("ImageMediaProbe")) {
        String currBucketDisplayName;
        double[] values = new double[2];
        double numImages = 0;
        Map<String, Integer> bucketImageMap = new HashMap<String, Integer>();
        int imageMediaSize = 0;

        ArrayList<BasicLog> totalChunkLogs = DBReader.readLog_customized(tableName,
                "where profile_id = " + profileId + " order by time_stamp asc", test);

        // IMPORTANT: Escape rule for exception
        // FIXME:
        if (totalChunkLogs.size() < 10) {
          System.err.println("Exception: profileId=" + profileId +
                  ", tableName=" + tableName);

          if (!nullFeatureProfileIdMap.containsKey(tableName)) {
            nullFeatureProfileIdMap.put(tableName,
                    new ArrayList<Integer>());
          }
          nullFeatureProfileIdMap.get(tableName).add(profileId);
          continue TableNameLoop;
        }

        for (int j = 0; j < expIds.size(); j++) {
          int expId = expIds.get(j);
          ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                  "where profile_id = " + profileId + " and expId = " + expId, test);

          for (int i = 0; i < tempChunkLogs.size(); i++) {
            ImageMediaLog log = (ImageMediaLog) tempChunkLogs.get(i);
            currBucketDisplayName = log.bucketDisplayName;
            numImages += 1;
            imageMediaSize += log.size;

            if (bucketImageMap.containsKey(currBucketDisplayName)) {
              bucketImageMap.put(currBucketDisplayName,
                      bucketImageMap.get(currBucketDisplayName) + 1);
            } else {
              bucketImageMap.put(currBucketDisplayName, 1);
            }
          }
        }

        List<Integer> numPhotoPerBuckets =
                new ArrayList<Integer>(bucketImageMap.values());
        Collections.sort(numPhotoPerBuckets);
        Collections.reverse(numPhotoPerBuckets);

        values[0] = numPhotoPerBuckets.get(0) / numImages;
        values[1] = Math.abs(imageMediaSize);
        printFeatureValue(values);
        feature.setValues_ImageMedia(tableName, values);
      }
      /**
       @author Kilho Kim
       @description VideoMediaProbe features
       */
      else if (tableName.equals("VideoMediaProbe")) {
        String currBucketDisplayName;
        double[] values = new double[3];
        int numVideos = 0;
        Map<String, Integer> bucketVideoMap = new HashMap<String, Integer>();
        int videoMediaSize = 0;
        int duration = 0;

        ArrayList<BasicLog> totalChunkLogs = DBReader.readLog_customized(tableName,
                "where profile_id = " + profileId + " order by time_stamp asc", test);

        // IMPORTANT: Escape rule for exception
        // FIXME:
        if (totalChunkLogs.size() < 10) {
          System.err.println("Exception: profileId=" + profileId +
                  ", tableName=" + tableName);
          if (!nullFeatureProfileIdMap.containsKey(tableName)) {
            nullFeatureProfileIdMap.put(tableName,
                    new ArrayList<Integer>());
          }
          nullFeatureProfileIdMap.get(tableName).add(profileId);
          continue TableNameLoop;
        }

        for (int j = 0; j < expIds.size(); j++) {
          int expId = expIds.get(j);
          ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                  "where profile_id = " + profileId + " and expId = " + expId, test);
          for (int i = 0; i < tempChunkLogs.size(); i++) {
            VideoMediaLog log = (VideoMediaLog) tempChunkLogs.get(i);
            currBucketDisplayName = log.bucketDisplayName;
            numVideos += 1;
            videoMediaSize += log.size;
            duration += log.duration;

            if (bucketVideoMap.containsKey(currBucketDisplayName)) {
              bucketVideoMap.put(currBucketDisplayName,
                      bucketVideoMap.get(currBucketDisplayName) + 1);
            } else {
              bucketVideoMap.put(currBucketDisplayName, 1);
            }
          }
        }
        List<Integer> numVideoPerBuckets =
                new ArrayList<Integer>(bucketVideoMap.values());
        Collections.sort(numVideoPerBuckets);
        Collections.reverse(numVideoPerBuckets);

        values[0] = numVideoPerBuckets.get(0) / numVideos;
        values[1] = Math.abs(videoMediaSize);
        values[2] = Math.abs(duration);
        printFeatureValue(values);
        feature.setValues_VideoMedia(tableName, values);
      }
      /**
       @author Kilho Kim
       @description AudioMediaProbe features
       */
      else if (tableName.equals("AudioMediaProbe")) {
        int[] values = new int[3];
        int numAudios = 0;
        int audioMediaSize = 0;
        int duration = 0;

        for (int j = 0; j < expIds.size(); j++) {
          int expId = expIds.get(j);
          ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                  "where profile_id = " + profileId + " and expId = " + expId, test);
          for (int i = 0; i < tempChunkLogs.size(); i++) {
            AudioMediaLog log = (AudioMediaLog) tempChunkLogs.get(i);
            numAudios += 1;
            audioMediaSize += log.size;
            duration += log.duration;
          }
        }
        values[0] = Math.abs(numAudios);
        values[1] = Math.abs(audioMediaSize);
        values[2] = Math.abs(duration);
        printFeatureValue(values);
        feature.setValues_AudioMedia(tableName, values);
      }
      /**
       @author Kilho Kim
       @description SmsProbe features
       */
      else if (tableName.equals("SmsProbe")) {
        double[] values = new double[3];
        double numSmss = 0;
        Map<Integer, Integer> addressSmsMap = new HashMap<Integer, Integer>();
        double top3AddressRatio = 0;
        double avgSmsInterval = 0;
        double sumSmsInterval = 0;
        int currAddress;
        double prevTimeStamp = -1.0;
        double currTimeStamp;

        ArrayList<BasicLog> totalChunkLogs = DBReader.readLog_customized(tableName,
                "where profile_id = " + profileId + " order by time_stamp asc", test);
        //                        "where profile_id = " + profileId + " and expId = " + expId + " order by time_stamp asc", test);

        // IMPORTANT: Escape rule for exception
        // FIXME:
        if (totalChunkLogs.size() < 10) {
          System.err.println("Exception: profileId=" + profileId +
                  ", tableName=" + tableName);
          if (!nullFeatureProfileIdMap.containsKey(tableName)) {
            nullFeatureProfileIdMap.put(tableName,
                    new ArrayList<Integer>());
          }
          nullFeatureProfileIdMap.get(tableName).add(profileId);
          continue TableNameLoop;
        }

        for (int i = 0; i < totalChunkLogs.size(); i++) {
          SmsLog log = (SmsLog) totalChunkLogs.get(i);
          numSmss += 1;
          currAddress = log.address;
          if (addressSmsMap.containsKey(currAddress)) {
            addressSmsMap.put(currAddress,
                    addressSmsMap.get(currAddress) + 1);
          } else {
            addressSmsMap.put(currAddress, 1);
          }

          currTimeStamp = log.timeStamp;
          if (prevTimeStamp > 0) {
            sumSmsInterval +=
                    Math.abs(currTimeStamp - prevTimeStamp);
          }
          prevTimeStamp = currTimeStamp;
        }

        List<Integer> numSmsPerAddresses =
                new ArrayList<Integer>(addressSmsMap.values());
        Collections.sort(numSmsPerAddresses);
        Collections.reverse(numSmsPerAddresses);
        // assert (numSmsPerAddresses.size() >= 3);
        double top3AddressSum = 0;
        double totalAddressSum = 0;
        for (int i = 0; i < 3; i++) {
          top3AddressSum += numSmsPerAddresses.get(i);
        }
        for (int i = 0; i < numSmsPerAddresses.size(); i++) {
          totalAddressSum += numSmsPerAddresses.get(i);
        }
        top3AddressRatio = top3AddressSum / totalAddressSum;
        avgSmsInterval = sumSmsInterval / (numSmss - 1);

        values[0] = Math.abs(numSmss);
        values[1] = Math.abs(top3AddressRatio);
        values[2] = Math.abs(avgSmsInterval);
        printFeatureValue(values);
        feature.setValues_Sms(tableName, values);
      }
      /**
       @author Kilho Kim
       @description CallLog features
       */
      else if (tableName.equals("CallLogProbe")) {
        double[] values = new double[6];
        double numCallLogs = 0;
        Map<Integer, Integer> numberCallLogMap =
                new HashMap<Integer, Integer>();
        double top3NumberRatio = 0;
        double avgCallInterval = 0;
        double sumCallInterval = 0;
        int currNumber;
        double prevTimeStamp = -1.0;
        double currTimeStamp;
        double callDuration = 0;
        double numUnknownCall = 0;
        double numCallOut = 0;

        ArrayList<BasicLog> totalChunkLogs = DBReader.readLog_customized(tableName,
                "where profile_id = " + profileId + " order by time_stamp asc", test);
        //                        "where profile_id = " + profileId + " and expId = " + expId + " order by time_stamp asc", test);

        // IMPORTANT: Escape rule for exception
        // FIXME:
        if (totalChunkLogs.size() < 10) {
          System.err.println("Exception: profileId=" + profileId +
                  ", tableName=" + tableName);
          if (!nullFeatureProfileIdMap.containsKey(tableName)) {
            nullFeatureProfileIdMap.put(tableName,
                    new ArrayList<Integer>());
          }
          nullFeatureProfileIdMap.get(tableName).add(profileId);
          continue TableNameLoop;
        }

        for (int i = 0; i < totalChunkLogs.size(); i++) {
          CallLog log = (CallLog) totalChunkLogs.get(i);
          numCallLogs += 1;
          callDuration += log.duration;
          if (log.numberType == 0) {
            numUnknownCall += 1;
          }
          if (log.type == 1) {
            numCallOut += 1;
          }
          currNumber = log.number;
          if (numberCallLogMap.containsKey(currNumber)) {
            numberCallLogMap.put(currNumber,
                    numberCallLogMap.get(currNumber) + 1);
          } else {
            numberCallLogMap.put(currNumber, 1);
          }

          currTimeStamp = log.timeStamp;
          if (prevTimeStamp > 0) {
            sumCallInterval +=
                    Math.abs(currTimeStamp - prevTimeStamp);
          }
          prevTimeStamp = currTimeStamp;
        }

        List<Integer> numCallPerNumbers =
                new ArrayList<Integer>(numberCallLogMap.values());
        Collections.sort(numCallPerNumbers);
        Collections.reverse(numCallPerNumbers);
        // assert (numSmsPerAddresses.size() >= 3);
        double top3NumberSum = 0;
        double totalNumberSum = 0;
        for (int i = 0; i < 3; i++) {
          top3NumberSum += numCallPerNumbers.get(i);
        }
        for (int i = 0; i < numCallPerNumbers.size(); i++) {
          totalNumberSum += numCallPerNumbers.get(i);
        }
        top3NumberRatio = top3NumberSum / totalNumberSum;
        avgCallInterval = sumCallInterval / (numCallLogs - 1);

        double unknownCallRatio = numUnknownCall / numCallLogs;
        double callOutRatio = numCallOut / numCallLogs;

        values[0] = Math.abs(numCallLogs);
        values[1] = Math.abs(callDuration);
        values[2] = Math.abs(top3NumberRatio);
        values[3] = Math.abs(unknownCallRatio);
        values[4] = Math.abs(callOutRatio);
        values[5] = Math.abs(avgCallInterval);
        printFeatureValue(values);
        feature.setValues_CallLog(tableName, values);
      }
    }


    return feature;
  }

  ////////////////////////////////////////////////////////////////////////////////////////////////
  private void printFeatureValue(int[] values) {
    for (int i = 0; i < values.length; i++) {
      System.out.print(values[i] + " ");
      if (i == values.length -1) System.out.println("");
    }
    System.out.println("");
  }

  private void printFeatureValue(double[] values) {
    for (int i = 0; i < values.length; i++) {
      System.out.print(values[i] + " ");
      if (i == values.length -1) System.out.println("");
    }
    System.out.println("");
  }

  private HashMap<Integer, Feature> fillMissingFeatures(
          HashMap<Integer, Feature> users,
          Map<String, List<Integer>> nullFeatureProfileIdMap,
          ArrayList<Integer> profileIds,
          boolean test) {

    // IMPORTANT: Put values into null feature for some profileIds
    //            an avg value for the other profileId's feature values
    for (String tableName : nullFeatureProfileIdMap.keySet()) {
      List<Integer> nullProfileIds =
              nullFeatureProfileIdMap.get(tableName);

      double avgValues[];
      switch (tableName) {
        case "ImageMediaProbe":
          avgValues = new double[2];
          break;
        case "VideoMediaProbe":
          avgValues = new double[3];
          break;
        case "SmsProbe":
          avgValues = new double[3];
          break;
        case "CallLogProbe":
          avgValues = new double[6];
          break;
        default:
          avgValues = new double[3];
          break;
      }

      int numProfileIds = 0;
      // Compute the average values for current feature
      for (int i = 0; i < profileIds.size(); i++) {
        int currProfileId = profileIds.get(i);
        if (!nullProfileIds.contains(currProfileId)) {
          numProfileIds += 1;
          NormalFeature currFeature = (NormalFeature)users.get(currProfileId);
          switch (tableName) {
            case "ImageMediaProbe":
              double[] imageMediaValues = {
                      currFeature.top1BucketPhotoRatio,
                      currFeature.imageMediaSize
              };
              for (int j = 0; j < imageMediaValues.length; j++) {
                avgValues[j] += imageMediaValues[j];
              }
              break;
            case "VideoMediaProbe":
              double[] videoMediaValues = {
                      currFeature.top1BucketVideoRatio,
                      currFeature.videoMediaSize,
                      currFeature.videoDuration
              };
              for (int j = 0; j < videoMediaValues.length; j++) {
                avgValues[j] += videoMediaValues[j];
              }
              break;
            case "SmsProbe":
              double[] smsValues = {
                      currFeature.numSmss,
                      currFeature.top3AddressRatio,
                      currFeature.avgSmsInterval
              };
              for (int j = 0; j < smsValues.length; j++) {
                avgValues[j] += smsValues[j];
              }
              break;
            case "CallLogProbe":
              double[] callLogValues = {
                      currFeature.numCallLogs,
                      currFeature.callDuration,
                      currFeature.top3NumberRatio,
                      currFeature.unknownCallRatio,
                      currFeature.callOutRatio,
                      currFeature.avgCallInterval
              };
              for (int j = 0; j < callLogValues.length; j++) {
                avgValues[j] += callLogValues[j];
              }
              break;
            default:
              break;
          }
        }
      }
      for (int j = 0; j < avgValues.length; j++) {
        avgValues[j] /= numProfileIds;
      }

      // Put the average value for missing features
      for (int i = 0; i < nullProfileIds.size(); i++) {
        int nullProfileId = nullProfileIds.get(i);
        String nullProfileIdUserLabel =
                DBReader.readLabel(labelName, nullProfileId, test);
        NormalFeature avgFeature = (NormalFeature)users.get(nullProfileId);
        printFeatureValue(avgValues);
        switch (tableName) {
          case "ImageMediaProbe":
            avgFeature.setValues_ImageMedia(tableName, avgValues);
            break;
          case "VideoMediaProbe":
            avgFeature.setValues_VideoMedia(tableName, avgValues);
          case "SmsProbe":
            avgFeature.setValues_Sms(tableName, avgValues);
            break;
          case "CallLogProbe":
            avgFeature.setValues_CallLog(tableName, avgValues);
            break;
          default:
            break;
        }
        users.put(nullProfileId, avgFeature);
      }
    }

    return users;
  }
}
