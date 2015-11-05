package project_team2;

import project_team2.util.Keys;
import project_team2.util.Converters;
import operator.ReadWriteInstances;
import structure.weka.Algorithm;
import structure.weka.Experiment;
import structure.weka.Filters;
import project_team2.util.Keys;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.core.Instances;
import weka.filters.Filter;

import java.util.ArrayList;
import java.util.Random;

/**
 * Created by Ethan on 2015-09-01.
 */
public class WekaClassifier {
    public static void main(String[] args) {
        WekaClassifier weka = new WekaClassifier();
        String labelName = Keys.LABEL_NAME;
        Experiment exp = new Experiment(labelName, true);
        exp.dataSetNames.add(Keys.FILE_NAME);
        // TODO: add dataSetNames here

        Algorithm alg1 = new Algorithm(labelName, 0, 0);
        alg1.filterNames_forWholeSet.add("Normalize");
        alg1.filterOptions_forWholeSet.add("-S 1.0 -T 0.0");
        alg1.filterSupervised_forWholeSet.add(false);

        alg1.filterNames_forTrainingSet.add("AttributeSelection");
        alg1.filterOptions_forTrainingSet.add("-E \"weka.attributeSelection.CfsSubsetEval \" -S \"weka.attributeSelection.BestFirst -D 1 -N 5\"");
        alg1.filterSupervised_forTrainingSet.add(true);

        alg1.classifierNames.add("NaiveBayes");
        alg1.classifierOptions.add(null);
        alg1.classifierNames.add("Logistic");
        alg1.classifierOptions.add("-R 1.0E-8 -M -1");
        alg1.classifierNames.add("MultilayerPerceptron");
        alg1.classifierOptions.add("-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a");
        alg1.classifierNames.add("SimpleLogistic");
        alg1.classifierOptions.add("-I 0 -M 500 -H 50 -W 0.0");
        alg1.classifierNames.add("IBk");
        alg1.classifierOptions.add("-K 1 -W 0 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\"");
        alg1.classifierNames.add("PART");
        alg1.classifierOptions.add("-M 2 -C 0.25 -Q 1");
        alg1.classifierNames.add("J48");
        alg1.classifierOptions.add("-C 0.25 -M 2");
        alg1.classifierNames.add("LMT");
        alg1.classifierOptions.add("-I -1 -M 15 -W 0.0");
        alg1.classifierNames.add("RandomForest");
        alg1.classifierOptions.add("-I 100 -K 0 -S 1");
        alg1.classifierNames.add("REPTree");
        alg1.classifierOptions.add("-M 2 -V 0.001 -N 3 -S 1 -L -1");

        exp.algorithms.add(alg1);

        // TODO: you can add more algorithms to exp here

        weka.runExperiment(exp, true, false, true);
    }

    public void runExperiment(Experiment exp, boolean detailedRun, boolean consoleWrite, boolean summaryWrite) {
        for (int i = 0; i < exp.dataSetNames.size(); i++) {
            for (int j = 0; j < exp.algorithms.size(); j++) {
                runAlgorithm(exp, exp.dataSetNames.get(i), exp.algorithms.get(j), detailedRun, consoleWrite);
            }
        }
        if (summaryWrite) exp.writeTotalResults();
    }

    public void runAlgorithm(Experiment exp, String dataSetName,
                             Algorithm alg, boolean detailedRun, boolean consoleWrite) {
        Instances dataSet = ReadWriteInstances.readFile(Keys.SAVE_PATH + dataSetName + "." + Keys.EXT);
        Filter filter_forWholeSet = alg.getFilter_forWholeSet();
        Instances filteredDataSet = filterDataSet(dataSet, filter_forWholeSet);
        Classifier[] clsArr = alg.getClassifiers();
        for (int i = 0; i < clsArr.length; i++) {
            Classifier cls = clsArr[i];
            ArrayList<double[]> errors = null;
            double trError = 0;
            double teError = 0;
            if (alg.evaluationMode == 0) {
                Instances[] datSets = dataSetPartition(dataSet, 0.6, new Random(1));
                try {
                    cls.buildClassifier(datSets[0]);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                trError = dedicatedSetValidationOnlyAcc(datSets[0], datSets[0], cls, consoleWrite);
                teError = dedicatedSetValidationOnlyAcc(datSets[0], datSets[1], cls, consoleWrite);
                exp.addResult(dataSetName, alg, i, trError, teError,
                        filteredDataSet.numInstances(), filteredDataSet.numAttributes());
            } else {
                if (alg.evaluationMode == 1) {
                    errors = crossValidation(filteredDataSet, cls, alg.foldNum, detailedRun, consoleWrite);
                } else {
                    errors = leaveOneOutValidation(filteredDataSet, cls, detailedRun, consoleWrite);
                }
                for (double[] errorArr : errors) {
                    trError += errorArr[0];
                    teError += errorArr[1];
                }
                exp.addResult(dataSetName, alg, i, trError / errors.size(), teError / errors.size(),
                        filteredDataSet.numInstances(), filteredDataSet.numAttributes());
            }
        }
    }

    public Instances filterDataSet(Instances dataSet, String filterName, String options, boolean supervised) {
        Filter filter = null;
        try {
            filter = Filters.getFilter(filterName, options, supervised);
            filter.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, filter);
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public Instances filterDataSet(Instances dataSet, Filter filter) {
        try {
            filter.setInputFormat(dataSet);
            dataSet = Filter.useFilter(dataSet, filter);
            dataSet.setClassIndex(dataSet.numAttributes() - 1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dataSet;
    }

    public double dedicatedSetValidationOnlyAcc(Instances train, Instances test, Classifier clsTrained, boolean consoleWrite) {
        try {
            Evaluation eval = new Evaluation(train);
            eval.evaluateModel(clsTrained, test);
            if (consoleWrite) {
                System.out.println(eval.toSummaryString());
            }
            return eval.errorRate();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }

    public ArrayList<double[]> crossValidation(Instances dataSet, Classifier cls, int numFolds,
                                               boolean detailedRun, boolean consoleWrite) {
        ArrayList<double[]> errors = null;
        try {
            Random random = new Random(1);
            errors = new ArrayList<>();
            if (detailedRun) {
                Instances[][] partitions = dataSetPartitionForCV(dataSet, numFolds, random);
                for (int i = 0; i < numFolds; ++i) {
                    cls.buildClassifier(partitions[i][0]);
                    double trError = dedicatedSetValidationOnlyAcc(partitions[i][0], partitions[i][0], cls, consoleWrite);
                    double teError = dedicatedSetValidationOnlyAcc(partitions[i][0], partitions[i][1], cls, consoleWrite);
                    double[] errorArr = {trError, teError};
                    errors.add(errorArr);
                }
            } else {
                // if you want apply filter or attribute selection, then you should use meta classifier.
                Evaluation eval = new Evaluation(dataSet);
                eval.crossValidateModel(cls, dataSet, numFolds, random);
                if (consoleWrite) {
                    System.out.println(eval.toSummaryString());
                    System.out.println(eval.toClassDetailsString());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return errors;
    }

    public ArrayList<double[]> leaveOneOutValidation(Instances dataSet, Classifier cls, boolean detailedRun, boolean consoleWrite) {
        return crossValidation(dataSet, cls, dataSet.numInstances(), detailedRun, consoleWrite);
    }

    private Instances[][] dataSetPartitionForCV(Instances dataSet, int numFolds, Random random) {
        Instances[][] partitions = new Instances[numFolds][2];
        Instances shuffledDataSet = new Instances(dataSet);
        shuffledDataSet.randomize(random);
        if (shuffledDataSet.classAttribute().isNominal()) {
            shuffledDataSet.stratify(numFolds);
        }

        for (int i = 0; i < numFolds; ++i) {
            Instances trainSet = shuffledDataSet.trainCV(numFolds, i, random);
            Instances testSet = shuffledDataSet.testCV(numFolds, i);
            partitions[i][0] = trainSet;
            partitions[i][1] = testSet;
        }
        return partitions;
    }

    private Instances[] dataSetPartition(Instances dataSet, double trRatio, Random random) {
        int trainSize = (int) Math.round(dataSet.numInstances() * trRatio);
        int testSize = dataSet.numInstances() - trainSize;

        Instances[] partitions = new Instances[2];
        Instances shuffledDataSet = new Instances(dataSet);
        shuffledDataSet.randomize(random);

        Instances trainSet = new Instances(shuffledDataSet, 0, trainSize);
        Instances testSet = new Instances(shuffledDataSet, trainSize, testSize);
        partitions[0] = trainSet;
        partitions[1] = testSet;
        return partitions;

    }

    public static Instances classifyDataSet(Instances dataSet, Classifier clsTrained) {
        Instances predicted = new Instances(dataSet);
        try {
            int numClassZero = 0, numClassOne = 1, numClassTwo = 2;
            for (int i = 0; i < predicted.numInstances(); i++) {
                double clsLabel = 0;
                clsLabel = clsTrained.classifyInstance(predicted.instance(i));
                String classNum = dataSet.classAttribute().value((int)clsLabel);
                if (classNum.equals(Converters.UNDER_55)) numClassZero++;
                else if (classNum.equals(Converters.OVER_55_UNDER_70)) numClassOne++;
                else /*(classNum.equals(Converters.OVER_70))*/ numClassTwo++;
                predicted.instance(i).setClassValue(Converters.classNumToWeight(classNum));
            }
            System.out.println("WekaClassifier.classifyDataSet()/ " +
                                "numClassZero=" + numClassZero +
                                ", numClassOne=" + numClassOne +
                                ", numClassTwo=" + numClassTwo);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return predicted;
    }
}
