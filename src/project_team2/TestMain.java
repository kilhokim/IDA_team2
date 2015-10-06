package project_team2;

import operator.ProjectEvaluator;
import structure.weka.Classifiers;
import weka.classifiers.Classifier;
import weka.core.Instances;

import java.util.HashMap;

/**
 * Created by Ethan on 2015-09-23.
 */
public class TestMain {

    public static void main(String[] args) {
        DataSetGenerator dataSetGen = new DataSetGenerator();
        HashMap<Integer, Feature> trUsers = dataSetGen.generateDataSet(false);
        for (Integer i : trUsers.keySet()) {
            System.out.println(i + ": " + trUsers.get(i));
        }
        Instances trainingSet = dataSetGen.transformToInstances(trUsers);
        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);
        // System.out.println("TestMain.main(): trainingSet.toSummaryString()=" + trainingSet.toSummaryString());
        System.out.println("TestMain.main(): trainingSet.toString()=" + trainingSet.toString());

        Classifier cls = Classifiers.getClassifier("Logistic");
        // Classifier cls = Classifiers.getClassifier("NaiveBayes");
        try {
            cls.buildClassifier(trainingSet);
        } catch (Exception e) {
            e.printStackTrace();
        }


        ////////////////////////////////////////////////////////////////////////////////////////////////
        //////////////////////////////////    Should be included!     //////////////////////////////////
        ProjectEvaluator.runTest(dataSetGen, cls);
        ////////////////////////////////////////////////////////////////////////////////////////////////
    }
}
