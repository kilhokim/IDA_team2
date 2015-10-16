package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import operator.ReadWriteInstances;
import structure.log.BasicLog;
import structure.log.deviceInteraction.ApplicationsLog;
import structure.log.deviceInteraction.ImageMediaLog;
import structure.log.motion.AccelerometerLog;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by Ethan on 2015-09-23.
 */
public class DataSetGenerator {

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
    private boolean CallLogProbe = false;
    private boolean SmsProbe = false;
    private boolean ContactProbe = false;

    // device interaction
    private boolean BrowserBookmarksProbe = false;
    private boolean BrowserSearchesProbe = false;
    private boolean ImageMediaProbe = true;
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

    public DataSetGenerator() {
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
                    !field.getName().equals("batchProcess")) {
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
        DataSetGenerator dataSetGen = new DataSetGenerator();
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
                for (String tableName : tableNames) {
                    Feature tempFeature = generateFeature_batchProcess(tableName, profileId, tempUserLabel, test);
                    users.put(profileId, tempFeature);
                }
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

    private Feature generateFeature(HashMap<String, ArrayList<BasicLog>> userLogs, String label) {
        Feature feature = new Feature();
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
        return feature;
    }

    public Feature generateFeature_batchProcess(String tableName, int profileId, String label, boolean test) {
        Feature feature = new Feature();
        feature.setLabel(label);
        ArrayList<Integer> expIds = DBReader.readExpIds(tableName, profileId, test);

        if (tableName.equals("AccelerometerSensorProbe")) {
            double avgX = 0;
            double avgY = 0;
            double avgZ = 0;
            int logSize = 0;
            for (int j = 0; j < expIds.size(); j++) {
                int expId = expIds.get(j);
                ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                        "where profile_id = " + profileId + " and expId = " + expId, test);
                for (int i = 0; i < tempChunkLogs.size(); i++) {
                    AccelerometerLog log = (AccelerometerLog) tempChunkLogs.get(i);
                    avgX += log.x;
                    avgY += log.y;
                    avgZ += log.z;
                }
                logSize += tempChunkLogs.size();
            }
            avgX /= logSize;
            avgY /= logSize;
            avgZ /= logSize;
            double[] values = new double[]{avgX, avgY, avgZ};
            feature.setValues_Accelerometer(tableName, values);
        }
        /**
         @author Kilho Kim
         @description ApplicationsProbe features
         */
        else if (tableName.equals("ApplicationsProbe")) {
            int logSize = 0;
            String currCategory;
            int[] values = new int[Feature.categoryNames.length];

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

                          currCategory =
                            project_team2.util.Parser
                              .parseCategory(GOOGLE_PLAY_URL,
                                log.packageName, CATEGORY_CSS_QUERY);
                          int categoryIdx =
                            Feature.categoryMap.get(currCategory);
                          values[categoryIdx] += 1;
                        } catch (IOException e) {
                            continue;
//                e.printStackTrace();
                        } catch (NullPointerException e) {
                            System.err.print("Not Found");
                            continue;
                        }
                    }
                    logSize += tempChunkLogs.size();
                }
                System.out.println("");
                for (int i = 0; i < values.length; i++) {
                   System.out.print(values[i] + " ");
                   if (i == values.length -1) System.out.println("");
                }
                feature.setValues_Applications(tableName, values);
        }
        /**
         @author Kilho Kim
         @description ImageMediaProbe features
         */
        else if (tableName.equals("ImageMediaProbe")) {
            int logSize = 0;
            // int[] values = new int[Feature.categoryNames.length];

            for (int j = 0; j < expIds.size(); j++) {
                int expId = expIds.get(j);
                ArrayList<BasicLog> tempChunkLogs = DBReader.readLog_customized(tableName,
                        "where profile_id = " + profileId + " and expId = " + expId, test);
                for (int i = 0; i < tempChunkLogs.size(); i++) {
                    ImageMediaLog log = (ImageMediaLog) tempChunkLogs.get(i);

                    log.bucketDisplayName




                }
                logSize += tempChunkLogs.size();
            }
            avgX /= logSize;
            avgY /= logSize;
            avgZ /= logSize;
            double[] values = new double[]{avgX, avgY, avgZ};
            feature.setValues_Accelerometer(tableName, values);


        }
        // else if
        return feature;
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
