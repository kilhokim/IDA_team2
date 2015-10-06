package structure.weka;

import weka.classifiers.Classifier;
import weka.filters.Filter;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Ethan on 2015-09-09.
 */
public class Algorithm {
    public String labelName;
    public ArrayList<String> filterNames_forWholeSet = new ArrayList<String>();
    public ArrayList<String> filterOptions_forWholeSet = new ArrayList<String>();
    public ArrayList<Boolean> filterSupervised_forWholeSet = new ArrayList<Boolean>();
    public ArrayList<String> filterNames_forTrainingSet = new ArrayList<String>();
    public ArrayList<String> filterOptions_forTrainingSet = new ArrayList<String>();
    public ArrayList<Boolean> filterSupervised_forTrainingSet = new ArrayList<Boolean>();
    public ArrayList<String> classifierNames = new ArrayList<String>();
    public ArrayList<String> classifierOptions = new ArrayList<String>();
    public int evaluationMode;  // 0: tr & te, 1: CV, 2: LOO
    public int foldNum;

    public Algorithm(String labelName, int evaluationMode, int foldNum) {
        this.labelName = labelName;
        this.evaluationMode = evaluationMode;
        this.foldNum = foldNum;
    }

    public Filter getFilter_forWholeSet() {
        String[] filterNames = filterNames_forWholeSet.toArray(new String[filterNames_forWholeSet.size()]);
        String[] filterOptions = filterOptions_forWholeSet.toArray(new String[filterOptions_forWholeSet.size()]);
        Boolean[] filterSupervised = filterSupervised_forWholeSet.toArray(new Boolean[filterSupervised_forWholeSet.size()]);
        return Filters.getMultiFilter(filterNames, filterOptions, filterSupervised);
    }

    public Filter getFilter_forTrainingSet() {
        String[] filterNames = filterNames_forTrainingSet.toArray(new String[filterNames_forTrainingSet.size()]);
        String[] filterOptions = filterOptions_forTrainingSet.toArray(new String[filterOptions_forTrainingSet.size()]);
        Boolean[] filterSupervised = filterSupervised_forTrainingSet.toArray(new Boolean[filterSupervised_forTrainingSet.size()]);
        return Filters.getMultiFilter(filterNames, filterOptions, filterSupervised);
    }

    public Classifier[] getClassifiers() {
        String[] filterNames = filterNames_forTrainingSet.toArray(new String[filterNames_forTrainingSet.size()]);
        String[] filterOptions = filterOptions_forTrainingSet.toArray(new String[filterOptions_forTrainingSet.size()]);
        Boolean[] filterSupervised = filterSupervised_forTrainingSet.toArray(new Boolean[filterSupervised_forTrainingSet.size()]);
        String[] clNamesArr = classifierNames.toArray(new String[classifierNames.size()]);
        String[] clOptionsArr = classifierOptions.toArray(new String[classifierOptions.size()]);
        Classifier[] classifiers = new Classifier[clNamesArr.length];
        for (int i = 0; i < clNamesArr.length; i++) {
            classifiers[i] = Classifiers.getFilteredClassifier(clNamesArr[i], clOptionsArr[i], filterNames,
                    filterOptions, filterSupervised);
        }
        return classifiers;
    }

    public String getHeader() {
        String header = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            if (i > 0) header += ",";
            header += fields[i].getName();
        }
        return header;
    }

    public Algorithm getClone(Algorithm alg) {
        Algorithm newAlg = new Algorithm(alg.labelName, alg.evaluationMode, alg.foldNum);
        for (String fNameWhole : alg.filterNames_forWholeSet) newAlg.filterNames_forWholeSet.add(fNameWhole);
        for (String fOptionWhole : alg.filterOptions_forWholeSet) newAlg.filterOptions_forWholeSet.add(fOptionWhole);
        for (boolean fSupWhole : alg.filterSupervised_forWholeSet) newAlg.filterSupervised_forWholeSet.add(fSupWhole);
        for (String fNameTr : alg.filterNames_forTrainingSet) newAlg.filterNames_forTrainingSet.add(fNameTr);
        for (String fOptionTr : alg.filterOptions_forTrainingSet) newAlg.filterOptions_forTrainingSet.add(fOptionTr);
        for (boolean fSupTr : alg.filterSupervised_forTrainingSet) newAlg.filterSupervised_forTrainingSet.add(fSupTr);
        for (String clName : alg.classifierNames) newAlg.classifierNames.add(clName);
        for (String clOption : alg.classifierOptions) newAlg.classifierOptions.add(clOption);
        return newAlg;
    }

    public String printAlgorithm(int order) {
        String line = "";
        Field[] fields = this.getClass().getDeclaredFields();
        for (int k = 0; k < fields.length; k++) {
            if (k > 0) line += ",";
            String term = "";
            if (fields[k].getType() == ArrayList.class) {
                ArrayList<String> values = null;
                try {
                    values = (ArrayList<String>) fields[k].get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                if (fields[k].getName().equals("classifierNames") || fields[k].getName().equals("classifierOptions")) {
                    term += values.get(order);
                } else {
                    for (int l = 0; l < values.size(); l++) {
                        if (l > 0) term += " / ";
                        term += "" + String.valueOf(values.get(l));
                    }
                }
                if (term.contains(",")) term = "\"" + term + "\"";
            } else {
                try {
                    term += fields[k].get(this);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
            line += term;
        }
        return line;
    }
}
