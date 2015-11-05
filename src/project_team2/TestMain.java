package project_team2;

import operator.ProjectEvaluator;
import structure.weka.Classifiers;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import project_team2.util.Keys;

/**
 * Created by Ethan on 2015-09-23.
 */
public class TestMain {

    public static void main(String[] args) {
        // Conduct training with non-sensor data
//        NormalDataSetGenerator normalDataSetGen = new NormalDataSetGenerator();
//        HashMap<Integer, Feature> normalTrUsers = normalDataSetGen.generateDataSet(false);
//        // IMPORTANT: Re-initialize nullFeatureProfileIdMap
//        normalDataSetGen.nullFeatureProfileIdMap = new HashMap<String, List<Integer>>();
//        for (Integer i : normalTrUsers.keySet()) {
//            System.out.println(i + ": " + normalTrUsers.get(i));
//        }
//        Instances normalTrainingSet = normalDataSetGen.transformToInstances(normalTrUsers);
//        normalTrainingSet.setClassIndex(normalTrainingSet.numAttributes() - 1);

        // Conduct training with sensor data
        SensorDataSetGenerator sensorDataSetGen = new SensorDataSetGenerator();
        HashMap<Integer, Feature> sensorTrUsers = sensorDataSetGen.generateDataSet(0);
        for (Integer i : sensorTrUsers.keySet()) {
            System.out.println(i + ": " + sensorTrUsers.get(i));
        }
        Instances sensorTrainingSet = sensorDataSetGen.transformToInstances(sensorTrUsers);
        sensorTrainingSet.setClassIndex(sensorTrainingSet.numAttributes() - 1);
//        for (int i = 0; i < sensorTrainingSet.size(); i++) {
//            System.out.println(sensorTrainingSet.get(i).classValue());
//        }

        // Fit model
//        Classifier normalCls = Classifiers.getClassifier("Logistic");
        Classifier sensorCls = Classifiers.getClassifier("Logistic");
//        Classifier sensorCls = new LinearRegression();
        try {
//            ((LinearRegression)sensorCls).setOptions(
//                    weka.core.Utils.splitOptions("-S 0 -R 1.0E-8 -num-decimal-places 4"));
//            normalCls.buildClassifier(normalTrainingSet);
            sensorCls.buildClassifier(sensorTrainingSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////    Should be included!     //////////////////////////////////
        // IMPORTANT: A new SensorDataSetGen which generates real 'weight' labels
        //            instead of class index of weight groups
        DataSetGenerator testSensorDataSetGen = new TestSensorDataSetGenerator();
        ProjectEvaluator.runTest(testSensorDataSetGen, sensorCls);
        ////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
