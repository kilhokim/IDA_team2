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
        Instances trainingSet = dataSetGen.transformToInstances(trUsers);
        trainingSet.setClassIndex(trainingSet.numAttributes() - 1);

        Classifier cls = Classifiers.getClassifier("NaiveBayes");
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
