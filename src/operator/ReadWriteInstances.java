package operator;

import dbConnection.DBConn;
import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVSaver;
import weka.core.converters.ConverterUtils.DataSource;
import weka.experiment.InstanceQuery;

import java.io.File;
import java.io.IOException;

/**
 * Created by Ethan on 2015-09-23.
 */
public class ReadWriteInstances {

    public static Instances readDB(String sqlQuery, String tableName, int sourceIndex) {
        /*
        proper mysql query is needed.
        sqlQuery : "SELECT * FROM AccelerometerSensorProbe"
        */
        if (!sqlQuery.contains(tableName)) {
            System.out.println("tableName must be included in sqlQuery!");
            System.out.println("tableName : " + tableName + " / sqlQuery : " + sqlQuery);
            System.exit(-1);
        }
        ProjectEvaluator.tableUsed(tableName);

        Instances dataSet = null;
        try {
            InstanceQuery query = new InstanceQuery();
            if(sourceIndex == 0){
                query.setDatabaseURL(DBConn.url);
                query.setUsername(DBConn.user);
                query.setPassword(DBConn.pwd);
            }
            else if(sourceIndex == 1){
                query.setDatabaseURL(DBConn.url_validation);
                query.setUsername(DBConn.user_validation);
                query.setPassword(DBConn.pwd_validation);
            }
            else if(sourceIndex == 2){
                query.setDatabaseURL(ProjectEvaluator.testUrl);
                query.setUsername(ProjectEvaluator.testUser);
                query.setPassword(ProjectEvaluator.testPwd);
            }
            else{
                throw new Exception("source index must be 0, 1 or 2!");
            }
            query.setQuery(sqlQuery);
            dataSet = query.retrieveInstances();
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public static Instances readFile(String filePath) {
        /*
        arff or csv files can be read.
        filePath : "dataSet\\accelerometer_instances.csv"
        filePath : "dataSet\\accelerometer_instances.arff"
        */
        Instances dataSet = null;
        try {
            dataSet = DataSource.read(filePath);
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public static void writeFile(Instances dataSet, String path, String fileName, String extension) {
        /*
        path : "dataSet\\"
        filename : "accelerometer_instances"
        extension : "csv"
        */
        try {
            if (extension.equals("arff")) {
                ArffSaver arffSaver = new ArffSaver();
                arffSaver.setInstances(dataSet);
                arffSaver.setFile(new File(path + fileName + "." + extension));
                arffSaver.writeBatch();
            } else if (extension.equals("csv")) {
                CSVSaver csvSaver = new CSVSaver();
                csvSaver.setInstances(dataSet);
                csvSaver.setFile(new File(path + fileName + "." + extension));
                csvSaver.writeBatch();
            } else {
                System.out.println("arff or csv only!");
                System.exit(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
