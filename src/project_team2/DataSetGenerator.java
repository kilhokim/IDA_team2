package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import operator.ReadWriteInstances;
import structure.log.BasicLog;
import structure.log.motion.AccelerometerLog;
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
public interface DataSetGenerator {

    public static String labelName = "weight";

    public ArrayList<String> getTableNames();

    public Instances transformToInstances(HashMap<Integer, Feature> dataSet);

    ////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////    Should be included!     //////////////////////////////////
    public HashMap<Integer, Feature> generateDataSet(int sourceIndex);
    ////////////////////////////////////////////////////////////////////////////////////////////////

    public Feature generateFeature(HashMap<String, ArrayList<BasicLog>> userLogs, Double label);

    public Feature generateFeature_batchProcess(ArrayList<String> tableNames, int profileId, Double label, int sourceIndex);
    ////////////////////////////////////////////////////////////////////////////////////////////////
}
