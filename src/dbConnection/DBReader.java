package dbConnection;

import operator.ProjectEvaluator;
import structure.log.BasicLog;
import structure.log.device.*;
import structure.log.deviceInteraction.*;
import structure.log.environment.*;
import structure.log.motion.*;
import structure.log.positioning.*;
import structure.log.social.CallLog;
import structure.log.social.ContactLog;
import structure.log.social.SmsLog;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Ethan on 2015-08-31.
 */
public class DBReader {

    /*
    read some DB table and return HashMap (key : profile id, value : list of logs)
    */

    public static HashMap<Integer, ArrayList<BasicLog>> readLog(String tableName, String whereClause, boolean test) {
        ProjectEvaluator.tableUsed(tableName);
        ResultSet rs = DBConn.execQuery("select * from " + tableName + " " + whereClause, test);
        HashMap<Integer, ArrayList<BasicLog>> eachUserLogs = new HashMap<Integer, ArrayList<BasicLog>>();
        try {
            while (rs.next()) {
                ArrayList<BasicLog> logs = null;
                if (eachUserLogs.containsKey(rs.getInt("profile_id"))) {
                    logs = eachUserLogs.get(rs.getInt("profile_id"));
                } else {
                    logs = new ArrayList<BasicLog>();
                    eachUserLogs.put(rs.getInt("profile_id"), logs);
                }

                switch (tableName) {
                    case "AccelerometerSensorProbe":
                        logs.add(new AccelerometerLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "GravitySensorProbe":
                        logs.add(new GravityLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "GyroscopeSensorProbe":
                        logs.add(new GyroscopeLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "LinearAccelerationSensorProbe":
                        logs.add(new LinearAccelerationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "OrientationSensorProbe":
                        logs.add(new LinearAccelerationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("azimuth"),
                                rs.getDouble("pitch"), rs.getDouble("roll")));
                        break;
                    case "RotationVectorSensorProbe":
                        logs.add(new RotationVectorLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("cosThetaOver2"),
                                rs.getDouble("xSinThetaOver2"), rs.getDouble("ySinThetaOver2"), rs.getDouble("zSinThetaOver2")));
                        break;

                    case "ProximitySensorProbe":
                        logs.add(new ProximityLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"), rs.getDouble("distance")));
                        break;
                    case "PressureSensorProbe":
                        logs.add(new PressureLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("pressure")));
                        break;
                    case "MagneticFieldSensorProbe":
                        logs.add(new MagneticFieldLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "LightSensorProbe":
                        logs.add(new LightLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("lux")));
                        break;
                    case "AudioFeaturesProbe":
                        logs.add(new AudioFeaturesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getDouble("diffSecs"), rs.getDouble("l1Norm"), rs.getDouble("l2Norm"), rs.getDouble("linfNorm")));
                        break;

                    case "CallLogProbe":
                        logs.add(new CallLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("duration"), rs.getDouble("number"), rs.getInt("numbertype"), rs.getInt("type")));
                        break;
                    case "SmsProbe":
                        logs.add(new SmsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("address"), rs.getInt("locked"), rs.getInt("protocol"), rs.getInt("read"),
                                rs.getInt("reply_path_present"), rs.getString("service_center"), rs.getInt("status"),
                                rs.getInt("thread_id"), rs.getInt("type")));
                        break;
                    case "ContactProbe":
                        logs.add(new ContactLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("values")));
                        break;


                    case "AccountsProbe":
                        logs.add(new AccountsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("type")));
                        break;
                    case "ApplicationsProbe":
                        logs.add(new ApplicationsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("packageName"), rs.getString("processName"),
                                rs.getInt("targetSdkVersion"), rs.getString("taskAffinity"), rs.getString("category"),
                                rs.getString("appCategory"), rs.getString("appName"), rs.getInt("appPrice")));
                        break;
                    case "AudioMediaProbe":
                        logs.add(new AudioMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getString("_size"), rs.getString("album"), rs.getString("artist"),
                                rs.getString("composer"), rs.getDouble("date_added"), rs.getInt("duration"), rs.getInt("is_alarm"),
                                rs.getInt("is_music"), rs.getInt("is_notification"), rs.getInt("is_ringtone"), rs.getString("mime_type"),
                                rs.getString("title"), rs.getInt("track"), rs.getString("year")));
                        break;
                    case "BrowserBookmarksProbe":
                        logs.add(new BrowserBookmarksLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("bookmark"), rs.getDouble("created"), rs.getString("title"), rs.getString("url"),
                                rs.getInt("visits")));
                        break;
                    case "BrowserSearchesProbe":
                        logs.add(new BrowserSearchesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("search")));
                        break;
                    case "ImageMediaProbe":
                        logs.add(new ImageMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getInt("_size"), rs.getString("bucket_display_name"),
                                rs.getDouble("date_added"), rs.getDouble("datetaken"), rs.getInt("isprivate"), rs.getDouble("latitude"),
                                rs.getDouble("longitude"), rs.getString("mime_type"), rs.getString("title"),
                                rs.getDouble("mini_thumb_magic"), rs.getInt("orientation")));
                        break;
                    case "ProcessStatisticsProbe":
                        logs.add(new ProcessStatisticsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("running_process_info")));
                        break;
                    case "RunningApplicationsProbe":
                        logs.add(new RunningApplicationsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("duration"), rs.getString("mClass"),
                                rs.getString("mPackage"), rs.getString("appCategory"), rs.getString("appName"),
                                rs.getInt("appPrice")));
                        break;
                    case "ScreenProbe":
                        logs.add(new ScreenLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("screenOn")));
                        break;
                    case "ServicesProbe":
                        logs.add(new ServicesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("process")));
                        break;
                    case "VideoMediaProbe":
                        logs.add(new VideoMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getString("_size"), rs.getString("album"), rs.getString("artist"),
                                rs.getInt("bookmark"), rs.getString("bucket_display_name"), rs.getDouble("date_added"), rs.getDouble("datetaken"),
                                rs.getInt("duration"), rs.getInt("isprivate"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                                rs.getString("mime_type"), rs.getString("title"), rs.getDouble("mini_thumb_magic"), rs.getString("resolution")));
                        break;

                    case "AndroidInfoProbe":
                        logs.add(new AndroidInfoLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("buildNumber"), rs.getString("firmwareVersion"), rs.getInt("sdk")));
                        break;
                    case "BatteryProbe":
                        logs.add(new BatteryLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("health"), rs.getInt("level"), rs.getInt("plugged"),
                                rs.getInt("present"), rs.getInt("scale"),
                                rs.getString("technology"), rs.getInt("status"), rs.getInt("temperature"), rs.getInt("voltage")));
                        break;
                    case "HardwareInfoProbe":
                        logs.add(new HardwareInfoLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("androidId"), rs.getString("bluetoothMac"), rs.getString("brand"),
                                rs.getDouble("deviceId"), rs.getString("model"), rs.getString("wifiMac")));
                        break;
                    case "NetworkSettingsProbe":
                        logs.add(new NetworkSettingsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("airPlaneModeOn"), rs.getInt("mobileDataOn"), rs.getInt("wifiOn")));
                        break;
                    case "SystemSettingsProbe":
                        logs.add(new SystemSettingsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accelerometerRotation"), rs.getInt("screenBrightness"),
                                rs.getInt("volumeAlarm"), rs.getInt("volumeMusic"), rs.getInt("volumeNotification"),
                                rs.getInt("volumeRing"), rs.getInt("volumeSystem"), rs.getInt("volumeVoice")));
                        break;
                    case "TelephonyProbe":
                        logs.add(new TelephonyLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("callState"), rs.getDouble("deviceId"),
                                rs.getString("deviceSoftwareVersion"), rs.getInt("hassIccCard"),
                                rs.getString("line1Number"), rs.getString("networkCountryIso"),
                                rs.getString("networkOperator"), rs.getString("networkOperatorName"),
                                rs.getInt("networkType"), rs.getInt("phoneType"), rs.getString("simCountryIso"),
                                rs.getString("simOperator"), rs.getString("simOperatorName"), rs.getDouble("simSerialNumber"),
                                rs.getInt("simState"), rs.getDouble("subscriberId"), rs.getString("voicemailAlphaTag"),
                                rs.getString("voicemailNumber")));
                        break;

                    case "BluetoothProbe":
                        logs.add(new BluetoothLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("appearance"), rs.getString("class_mClass"), rs.getString("device_mAddress"),
                                rs.getString("device_mRemoteBrsf"), rs.getString("device_mValueNREC"), rs.getString("device_mValueWBS"),
                                rs.getString("bluetooth_name"), rs.getString("RSSI")));
                        break;
                    case "CellTowerProbe":
                        logs.add(new CellTowerLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("cid"), rs.getInt("lac"), rs.getDouble("psc"), rs.getInt("type")));
                        break;
                    case "WifiProbe":
                        logs.add(new WifiLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("BSSID"), rs.getString("SSID"), rs.getString("capabilities"), rs.getInt("distanceCm"),
                                rs.getInt("distanceSdCm"), rs.getInt("frequency"), rs.getInt("level"), rs.getDouble("tsf")));
                        break;
                    case "SimpleLocationProbe":
                        logs.add(new SimpleLocationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("mAccuracy"), rs.getDouble("mLatitude"), rs.getDouble("mLongitude"), rs.getString("mProvider")));
                        break;
                    case "LocationProbe":
                        logs.add(new LocationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("mAccuracy"), rs.getDouble("mAltitude"), rs.getDouble("mBearing"),
                                rs.getDouble("mLatitude"), rs.getDouble("mLongitude"), rs.getDouble("mSpeed"),
                                rs.getString("mProvider"), rs.getInt("mHasAccuracy"), rs.getInt("mHasAltitude"),
                                rs.getInt("mHasBearing"), rs.getInt("mHasSpeed"),
                                rs.getInt("mIsFromMockProvider")));
                        break;
                }
            }
            rs.close();
            DBConn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eachUserLogs;
    }

    public static ArrayList<BasicLog> readLog_customized(String tableName, String whereClause, boolean test) {
        ProjectEvaluator.tableUsed(tableName);
        ResultSet rs = DBConn.execQuery("select * from " + tableName + " " + whereClause, test);
        ArrayList<BasicLog> logs = new ArrayList<BasicLog>();
        try {
            while (rs.next()) {
                switch (tableName) {
                    case "AccelerometerSensorProbe":
                        logs.add(new AccelerometerLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "GravitySensorProbe":
                        logs.add(new GravityLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "GyroscopeSensorProbe":
                        logs.add(new GyroscopeLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "LinearAccelerationSensorProbe":
                        logs.add(new LinearAccelerationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("x"),
                                rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "OrientationSensorProbe":
                        logs.add(new LinearAccelerationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("azimuth"),
                                rs.getDouble("pitch"), rs.getDouble("roll")));
                        break;
                    case "RotationVectorSensorProbe":
                        logs.add(new RotationVectorLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accuracy"), rs.getDouble("cosThetaOver2"),
                                rs.getDouble("xSinThetaOver2"), rs.getDouble("ySinThetaOver2"), rs.getDouble("zSinThetaOver2")));
                        break;

                    case "ProximitySensorProbe":
                        logs.add(new ProximityLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"), rs.getDouble("distance")));
                        break;
                    case "PressureSensorProbe":
                        logs.add(new PressureLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("pressure")));
                        break;
                    case "MagneticFieldSensorProbe":
                        logs.add(new MagneticFieldLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("x"), rs.getDouble("y"), rs.getDouble("z")));
                        break;
                    case "LightSensorProbe":
                        logs.add(new LightLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getInt("accuracy"), rs.getDouble("lux")));
                        break;
                    case "AudioFeaturesProbe":
                        logs.add(new AudioFeaturesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"), rs.getString("probe_name"),
                                rs.getInt("profile_id"), rs.getDouble("diffSecs"), rs.getDouble("l1Norm"), rs.getDouble("l2Norm"), rs.getDouble("linfNorm")));
                        break;

                    case "CallLogProbe":
                        logs.add(new CallLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("duration"), rs.getDouble("number"), rs.getInt("numbertype"), rs.getInt("type")));
                        break;
                    case "SmsProbe":
                        logs.add(new SmsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("address"), rs.getInt("locked"), rs.getInt("protocol"), rs.getInt("read"),
                                rs.getInt("reply_path_present"), rs.getString("service_center"), rs.getInt("status"),
                                rs.getInt("thread_id"), rs.getInt("type")));
                        break;
                    case "ContactProbe":
                        logs.add(new ContactLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("values")));
                        break;


                    case "AccountsProbe":
                        logs.add(new AccountsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("type")));
                        break;
                    case "ApplicationsProbe":
                        logs.add(new ApplicationsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("packageName"), rs.getString("processName"),
                                rs.getInt("targetSdkVersion"), rs.getString("taskAffinity"), rs.getString("category"),
                                rs.getString("appCategory"), rs.getString("appName"), rs.getInt("appPrice")));
                        break;
                    case "AudioMediaProbe":
                        logs.add(new AudioMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getString("_size"), rs.getString("album"), rs.getString("artist"),
                                rs.getString("composer"), rs.getDouble("date_added"), rs.getInt("duration"), rs.getInt("is_alarm"),
                                rs.getInt("is_music"), rs.getInt("is_notification"), rs.getInt("is_ringtone"), rs.getString("mime_type"),
                                rs.getString("title"), rs.getInt("track"), rs.getString("year")));
                        break;
                    case "BrowserBookmarksProbe":
                        logs.add(new BrowserBookmarksLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("bookmark"), rs.getDouble("created"), rs.getString("title"), rs.getString("url"),
                                rs.getInt("visits")));
                        break;
                    case "BrowserSearchesProbe":
                        logs.add(new BrowserSearchesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("search")));
                        break;
                    case "ImageMediaProbe":
                        logs.add(new ImageMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getInt("_size"), rs.getString("bucket_display_name"),
                                rs.getDouble("date_added"), rs.getDouble("datetaken"), rs.getInt("isprivate"), rs.getDouble("latitude"),
                                rs.getDouble("longitude"), rs.getString("mime_type"), rs.getString("title"),
                                rs.getDouble("mini_thumb_magic"), rs.getInt("orientation")));
                        break;
                    case "ProcessStatisticsProbe":
                        logs.add(new ProcessStatisticsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("running_process_info")));
                        break;
                    case "RunningApplicationsProbe":
                        logs.add(new RunningApplicationsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("duration"), rs.getString("mClass"),
                                rs.getString("mPackage"), rs.getString("appCategory"), rs.getString("appName"),
                                rs.getInt("appPrice")));
                        break;
                    case "ScreenProbe":
                        logs.add(new ScreenLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("screenOn")));
                        break;
                    case "ServicesProbe":
                        logs.add(new ServicesLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("process")));
                        break;
                    case "VideoMediaProbe":
                        logs.add(new VideoMediaLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("_display_name"), rs.getString("_size"), rs.getString("album"), rs.getString("artist"),
                                rs.getInt("bookmark"), rs.getString("bucket_display_name"), rs.getDouble("date_added"), rs.getDouble("datetaken"),
                                rs.getInt("duration"), rs.getInt("isprivate"), rs.getDouble("latitude"), rs.getDouble("longitude"),
                                rs.getString("mime_type"), rs.getString("title"), rs.getDouble("mini_thumb_magic"), rs.getString("resolution")));
                        break;

                    case "AndroidInfoProbe":
                        logs.add(new AndroidInfoLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("buildNumber"), rs.getString("firmwareVersion"), rs.getInt("sdk")));
                        break;
                    case "BatteryProbe":
                        logs.add(new BatteryLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("health"), rs.getInt("level"), rs.getInt("plugged"),
                                rs.getInt("present"), rs.getInt("scale"),
                                rs.getString("technology"), rs.getInt("status"), rs.getInt("temperature"), rs.getInt("voltage")));
                        break;
                    case "HardwareInfoProbe":
                        logs.add(new HardwareInfoLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("androidId"), rs.getString("bluetoothMac"), rs.getString("brand"),
                                rs.getDouble("deviceId"), rs.getString("model"), rs.getString("wifiMac")));
                        break;
                    case "NetworkSettingsProbe":
                        logs.add(new NetworkSettingsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("airPlaneModeOn"), rs.getInt("mobileDataOn"), rs.getInt("wifiOn")));
                        break;
                    case "SystemSettingsProbe":
                        logs.add(new SystemSettingsLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("accelerometerRotation"), rs.getInt("screenBrightness"),
                                rs.getInt("volumeAlarm"), rs.getInt("volumeMusic"), rs.getInt("volumeNotification"),
                                rs.getInt("volumeRing"), rs.getInt("volumeSystem"), rs.getInt("volumeVoice")));
                        break;
                    case "TelephonyProbe":
                        logs.add(new TelephonyLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("callState"), rs.getDouble("deviceId"),
                                rs.getString("deviceSoftwareVersion"), rs.getInt("hassIccCard"),
                                rs.getString("line1Number"), rs.getString("networkCountryIso"),
                                rs.getString("networkOperator"), rs.getString("networkOperatorName"),
                                rs.getInt("networkType"), rs.getInt("phoneType"), rs.getString("simCountryIso"),
                                rs.getString("simOperator"), rs.getString("simOperatorName"), rs.getDouble("simSerialNumber"),
                                rs.getInt("simState"), rs.getDouble("subscriberId"), rs.getString("voicemailAlphaTag"),
                                rs.getString("voicemailNumber")));
                        break;

                    case "BluetoothProbe":
                        logs.add(new BluetoothLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("appearance"), rs.getString("class_mClass"), rs.getString("device_mAddress"),
                                rs.getString("device_mRemoteBrsf"), rs.getString("device_mValueNREC"), rs.getString("device_mValueWBS"),
                                rs.getString("bluetooth_name"), rs.getString("RSSI")));
                        break;
                    case "CellTowerProbe":
                        logs.add(new CellTowerLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getInt("cid"), rs.getInt("lac"), rs.getDouble("psc"), rs.getInt("type")));
                        break;
                    case "WifiProbe":
                        logs.add(new WifiLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getString("BSSID"), rs.getString("SSID"), rs.getString("capabilities"), rs.getInt("distanceCm"),
                                rs.getInt("distanceSdCm"), rs.getInt("frequency"), rs.getInt("level"), rs.getDouble("tsf")));
                        break;
                    case "SimpleLocationProbe":
                        logs.add(new SimpleLocationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("mAccuracy"), rs.getDouble("mLatitude"), rs.getDouble("mLongitude"), rs.getString("mProvider")));
                        break;
                    case "LocationProbe":
                        logs.add(new LocationLog(rs.getInt("id"), rs.getDouble("time_stamp"), rs.getLong("expId"),
                                rs.getString("probe_name"), rs.getInt("profile_id"),
                                rs.getDouble("mAccuracy"), rs.getDouble("mAltitude"), rs.getDouble("mBearing"),
                                rs.getDouble("mLatitude"), rs.getDouble("mLongitude"), rs.getDouble("mSpeed"),
                                rs.getString("mProvider"), rs.getInt("mHasAccuracy"), rs.getInt("mHasAltitude"),
                                rs.getInt("mHasBearing"), rs.getInt("mHasSpeed"),
                                rs.getInt("mIsFromMockProvider")));
                        break;
                }
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return logs;
    }

    public static ArrayList<Integer> readProfileIds(boolean test){
        ArrayList<Integer> profileIds = new ArrayList<>();
        ResultSet rs = DBConn.execQuery("select distinct profile_id from profile_info", test);
        try {
            while(rs.next()){
                profileIds.add(rs.getInt("profile_id"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return profileIds;
    }

    public static ArrayList<Integer> readExpIds(String tableName, int profileId, boolean test){
        ArrayList<Integer> expIds = new ArrayList<>();
        ResultSet rs = DBConn.execQuery("select distinct expId from " + tableName +
                " where profile_id = " + profileId, test);
        try {
            while(rs.next()){
                expIds.add(rs.getInt("expId"));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return expIds;
    }


    public static String readLabel(String labelName, int profileId, boolean test) {
        String label = null;
        ResultSet rs = DBConn.execQuery("select " + labelName +
                " from profile_info where profile_id = " + profileId, test);
        try {
            rs.next();
            label = "" + rs.getString(labelName);
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return label;
    }
}
