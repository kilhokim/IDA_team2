package project_team2;

import dbConnection.DBConn;
import dbConnection.DBReader;
import project_team2.util.Keys;
import project_team2.util.Converters;
import structure.log.BasicLog;
import weka.core.Instances;

import java.lang.reflect.Field;
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
                      !field.getName().equals("deltaThresh")
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
              // FIXME:
							instValues[i] = Converters.classNumToWeight(tempLabel);
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
