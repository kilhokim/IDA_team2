package project_team2;

import operator.ProjectEvaluator;
import structure.weka.Classifiers;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

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
        HashMap<Integer, Feature> sensorTrUsers = sensorDataSetGen.generateDataSet(false);
        for (Integer i : sensorTrUsers.keySet()) {
            System.out.println(i + ": " + sensorTrUsers.get(i));
        }
        Instances sensorTrainingSet = sensorDataSetGen.transformToInstances(sensorTrUsers);
        sensorTrainingSet.setClassIndex(sensorTrainingSet.numAttributes() - 1);


        // Fit model
//        Classifier normalCls = Classifiers.getClassifier("Logistic");
        Classifier sensorCls = Classifiers.getClassifier("Logistic");
        // Classifier cls = Classifiers.getClassifier("NaiveBayes");
        try {
//            normalCls.buildClassifier(normalTrainingSet);
            sensorCls.buildClassifier(sensorTrainingSet);
        } catch (Exception e) {
            e.printStackTrace();
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////    Should be included!     //////////////////////////////////
        ProjectEvaluator.runTest(sensorDataSetGen, sensorCls);
        ////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
