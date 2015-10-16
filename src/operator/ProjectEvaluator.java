package operator;

import project_team2.DataSetGenerator;
import project_team2.Feature;
import project_team2.WekaClassifier;
import structure.log.device.*;
import structure.log.deviceInteraction.*;
import structure.log.environment.*;
import structure.log.motion.*;
import structure.log.positioning.*;
import structure.log.social.CallLog;
import structure.log.social.ContactLog;
import structure.log.social.SmsLog;
import util.Keys;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.HashMap;

/**
 * Created by Ethan on 2015-09-24.
 */
public class ProjectEvaluator {

    public static String testUrl = Keys.DB_URL;
    public static String testUser = Keys.DB_USERNAME;
    public static String testPwd = Keys.DB_PWD;

    public static boolean AccelerometerSensorProbe = false;
    public static boolean GravitySensorProbe = false;
    public static boolean LinearAccelerationSensorProbe = false;
    public static boolean GyroscopeSensorProbe = false;
    public static boolean OrientationSensorProbe = false;
    public static boolean RotationVectorSensorProbe = false;

    public static boolean BluetoothProbe = false;
    public static boolean WifiProbe = false;
    public static boolean CellTowerProbe = false;
    public static boolean LocationProbe = false;
    public static boolean SimpleLocationProbe = false;

    public static boolean MagneticFieldSensorProbe = false;
    public static boolean ProximitySensorProbe = false;
    public static boolean LightSensorProbe = false;
    public static boolean PressureSensorProbe = false;
    public static boolean AudioFeaturesProbe = false;

    public static boolean CallLogProbe = false;
    public static boolean SmsProbe = false;
    public static boolean ContactProbe = false;

    public static boolean BrowserBookmarksProbe = false;
    public static boolean BrowserSearchesProbe = false;
    public static boolean ImageMediaProbe = false;
    public static boolean VideoMediaProbe = false;
    public static boolean AudioMediaProbe = false;
    public static boolean RunningApplicationsProbe = false;
    public static boolean ApplicationsProbe = false;
    public static boolean ScreenProbe = false;
    public static boolean AccountsProbe = false;
    public static boolean ProcessStatisticsProbe = false;
    public static boolean ServicesProbe = false;

    public static boolean AndroidInfoProbe = false;
    public static boolean BatteryProbe = false;
    public static boolean HardwareInfoProbe = false;
    public static boolean NetworkSettingsProbe = false;
    public static boolean SystemSettingsProbe = false;
    public static boolean TelephonyProbe = false;

    public static void runTest(DataSetGenerator dataSetGen, Classifier trainedCls) {
        long startTime = System.currentTimeMillis();
        HashMap<Integer, Feature> teUsers = dataSetGen.generateDataSet(true);
        Instances testSet = dataSetGen.transformToInstances(teUsers);
        testSet.setClassIndex(testSet.numAttributes() - 1);

        Instances predictedSet = WekaClassifier.classifyDataSet(testSet, trainedCls);
        long endTime = System.currentTimeMillis();
        int timeCost = (int) (endTime - startTime);
        int privacyCost = computePrivacyCost();
        int energyCost = computeEnergyCost();
        double accuracy = computeAccuracy(testSet, predictedSet);
        System.out.println("test result");
        System.out.println("teUsers.size()=" + teUsers.size());
        System.out.println("time cost : " + timeCost);
        System.out.println("privacy cost : " + privacyCost);
        System.out.println("energy cost : " + energyCost);
        System.out.println("accuracy : " + accuracy);
    }

    public static void tableUsed(String tableName) {
        switch (tableName) {
            case "AccelerometerSensorProbe":
                AccelerometerSensorProbe = true;
                break;
            case "GravitySensorProbe":
                GravitySensorProbe = true;
                break;
            case "GyroscopeSensorProbe":
                GyroscopeSensorProbe = true;
                break;
            case "LinearAccelerationSensorProbe":
                LinearAccelerationSensorProbe = true;
                break;
            case "OrientationSensorProbe":
                OrientationSensorProbe = true;
                break;
            case "RotationVectorSensorProbe":
                RotationVectorSensorProbe = true;
                break;

            case "ProximitySensorProbe":
                ProximitySensorProbe = true;
                break;
            case "PressureSensorProbe":
                PressureSensorProbe = true;
                break;
            case "MagneticFieldSensorProbe":
                MagneticFieldSensorProbe = true;
                break;
            case "LightSensorProbe":
                LightSensorProbe = true;
                break;
            case "AudioFeaturesProbe":
                AudioFeaturesProbe = true;
                break;

            case "CallLogProbe":
                CallLogProbe = true;
                break;
            case "SmsProbe":
                SmsProbe = true;
                break;
            case "ContactProbe":
                ContactProbe = true;
                break;


            case "AccountsProbe":
                AccountsProbe = true;
                break;
            case "ApplicationsProbe":
                ApplicationsProbe = true;
                break;
            case "AudioMediaProbe":
                AudioMediaProbe = true;
                break;
            case "BrowserBookmarksProbe":
                BrowserBookmarksProbe = true;
                break;
            case "BrowserSearchesProbe":
                BrowserSearchesProbe = true;
                break;
            case "ImageMediaProbe":
                ImageMediaProbe = true;
                break;
            case "ProcessStatisticsProbe":
                ProcessStatisticsProbe = true;
                break;
            case "RunningApplicationsProbe":
                RunningApplicationsProbe = true;
                break;
            case "ScreenProbe":
                ScreenProbe = true;
                break;
            case "ServicesProbe":
                ServicesProbe = true;
                break;
            case "VideoMediaProbe":
                VideoMediaProbe = true;
                break;

            case "AndroidInfoProbe":
                AndroidInfoProbe = true;
                break;
            case "BatteryProbe":
                BatteryProbe = true;
                break;
            case "HardwareInfoProbe":
                HardwareInfoProbe = true;
                break;
            case "NetworkSettingsProbe":
                NetworkSettingsProbe = true;
                break;
            case "SystemSettingsProbe":
                SystemSettingsProbe = true;
                break;
            case "TelephonyProbe":
                TelephonyProbe = true;
                break;

            case "BluetoothProbe":
                BluetoothProbe = true;
                break;
            case "CellTowerProbe":
                CellTowerProbe = true;
                break;
            case "WifiProbe":
                WifiProbe = true;
                break;
            case "SimpleLocationProbe":
                SimpleLocationProbe = true;
                break;
            case "LocationProbe":
                LocationProbe = true;
                break;
        }
    }

    private static double computeAccuracy(Instances original, Instances predicted) {
        int rightCnt = 0;
        for (int i = 0; i < original.numInstances(); i++) {
            if (original.get(i).classValue() == predicted.get(i).classValue()) rightCnt++;
        }
        return (double) rightCnt / original.numInstances();
    }

    private static int computePrivacyCost() {
        int cost = 0;
        if (AccelerometerSensorProbe) cost += AccelerometerLog.privacy;
        if (GravitySensorProbe) cost += GravityLog.privacy;
        if (LinearAccelerationSensorProbe) cost += LinearAccelerationLog.privacy;
        if (GyroscopeSensorProbe) cost += GyroscopeLog.privacy;
        if (OrientationSensorProbe) cost += OrientationLog.privacy;
        if (RotationVectorSensorProbe) cost += RotationVectorLog.privacy;

        if (BluetoothProbe) cost += BluetoothLog.privacy;
        if (WifiProbe) cost += WifiLog.privacy;
        if (CellTowerProbe) cost += CellTowerLog.privacy;
        if (LocationProbe) cost += LocationLog.privacy;
        if (SimpleLocationProbe) cost += SimpleLocationLog.privacy;

        if (MagneticFieldSensorProbe) cost += MagneticFieldLog.privacy;
        if (ProximitySensorProbe) cost += ProximityLog.privacy;
        if (LightSensorProbe) cost += LightLog.privacy;
        if (PressureSensorProbe) cost += PressureLog.privacy;
        if (AudioFeaturesProbe) cost += AudioFeaturesLog.privacy;

        if (CallLogProbe) cost += CallLog.privacy;
        if (SmsProbe) cost += SmsLog.privacy;
        if (ContactProbe) cost += ContactLog.privacy;

        if (BrowserBookmarksProbe) cost += BrowserBookmarksLog.privacy;
        if (BrowserSearchesProbe) cost += BrowserSearchesLog.privacy;
        if (ImageMediaProbe) cost += ImageMediaLog.privacy;
        if (VideoMediaProbe) cost += VideoMediaLog.privacy;
        if (AudioMediaProbe) cost += AudioMediaLog.privacy;
        if (RunningApplicationsProbe) cost += RunningApplicationsLog.privacy;
        if (ApplicationsProbe) cost += ApplicationsLog.privacy;
        if (ScreenProbe) cost += ScreenLog.privacy;
        if (AccountsProbe) cost += AccountsLog.privacy;
        if (ProcessStatisticsProbe) cost += ProcessStatisticsLog.privacy;
        if (ServicesProbe) cost += ServicesLog.privacy;

        if (AndroidInfoProbe) cost += AndroidInfoLog.privacy;
        if (BatteryProbe) cost += BatteryLog.privacy;
        if (HardwareInfoProbe) cost += HardwareInfoLog.privacy;
        if (NetworkSettingsProbe) cost += NetworkSettingsLog.privacy;
        if (SystemSettingsProbe) cost += SystemSettingsLog.privacy;
        if (TelephonyProbe) cost += TelephonyLog.privacy;
        return cost;
    }

    private static int computeEnergyCost() {
        int cost = 0;
        if (AccelerometerSensorProbe) cost += AccelerometerLog.energyConsumption;
        if (GravitySensorProbe) cost += GravityLog.energyConsumption;
        if (LinearAccelerationSensorProbe) cost += LinearAccelerationLog.energyConsumption;
        if (GyroscopeSensorProbe) cost += GyroscopeLog.energyConsumption;
        if (OrientationSensorProbe) cost += OrientationLog.energyConsumption;
        if (RotationVectorSensorProbe) cost += RotationVectorLog.energyConsumption;

        if (BluetoothProbe) cost += BluetoothLog.energyConsumption;
        if (WifiProbe) cost += WifiLog.energyConsumption;
        if (CellTowerProbe) cost += CellTowerLog.energyConsumption;
        if (LocationProbe) cost += LocationLog.energyConsumption;
        if (SimpleLocationProbe) cost += SimpleLocationLog.energyConsumption;

        if (MagneticFieldSensorProbe) cost += MagneticFieldLog.energyConsumption;
        if (ProximitySensorProbe) cost += ProximityLog.energyConsumption;
        if (LightSensorProbe) cost += LightLog.energyConsumption;
        if (PressureSensorProbe) cost += PressureLog.energyConsumption;
        if (AudioFeaturesProbe) cost += AudioFeaturesLog.energyConsumption;

        if (CallLogProbe) cost += CallLog.energyConsumption;
        if (SmsProbe) cost += SmsLog.energyConsumption;
        if (ContactProbe) cost += ContactLog.energyConsumption;

        if (BrowserBookmarksProbe) cost += BrowserBookmarksLog.energyConsumption;
        if (BrowserSearchesProbe) cost += BrowserSearchesLog.energyConsumption;
        if (ImageMediaProbe) cost += ImageMediaLog.energyConsumption;
        if (VideoMediaProbe) cost += VideoMediaLog.energyConsumption;
        if (AudioMediaProbe) cost += AudioMediaLog.energyConsumption;
        if (RunningApplicationsProbe) cost += RunningApplicationsLog.energyConsumption;
        if (ApplicationsProbe) cost += ApplicationsLog.energyConsumption;
        if (ScreenProbe) cost += ScreenLog.energyConsumption;
        if (AccountsProbe) cost += AccountsLog.energyConsumption;
        if (ProcessStatisticsProbe) cost += ProcessStatisticsLog.energyConsumption;
        if (ServicesProbe) cost += ServicesLog.energyConsumption;

        if (AndroidInfoProbe) cost += AndroidInfoLog.energyConsumption;
        if (BatteryProbe) cost += BatteryLog.energyConsumption;
        if (HardwareInfoProbe) cost += HardwareInfoLog.energyConsumption;
        if (NetworkSettingsProbe) cost += NetworkSettingsLog.energyConsumption;
        if (SystemSettingsProbe) cost += SystemSettingsLog.energyConsumption;
        if (TelephonyProbe) cost += TelephonyLog.energyConsumption;
        return cost;
    }
}
