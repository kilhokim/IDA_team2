package structure.weka;

import weka.classifiers.Classifier;
import weka.classifiers.bayes.BayesNet;
import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.*;
import weka.classifiers.lazy.IBk;
import weka.classifiers.lazy.KStar;
import weka.classifiers.lazy.LWL;
import weka.classifiers.meta.*;
import weka.classifiers.rules.*;
import weka.classifiers.trees.*;
import weka.filters.MultiFilter;

/**
 * Created by Ethan on 2015-09-09.
 */
public class Classifiers {

    public static Classifier getClassifier(String classifierName, String options) {
        return getCustomizedClassifier(classifierName, options);
    }

    public static Classifier getClassifier(String classifierName) {
        return getDefaultClassifier(classifierName);
    }

    public static FilteredClassifier getFilteredClassifier(String classifierName, String classifierOptions,
                                                           String[] filterNames, String[] filterOptions, Boolean[] supervised) {
        FilteredClassifier wrapperCls = new FilteredClassifier();
        MultiFilter multiFilter = Filters.getMultiFilter(filterNames, filterOptions, supervised);
        wrapperCls.setFilter(multiFilter);
        if (classifierOptions != null) wrapperCls.setClassifier(getClassifier(classifierName, classifierOptions));
        else wrapperCls.setClassifier(getClassifier(classifierName));
        return wrapperCls;
    }

    private static Classifier getCustomizedClassifier(String classifierName, String options) {
        Classifier cls = null;
        try {
            switch (classifierName) {
                // bayes
                case "BayesNet":
                    cls = new BayesNet();
                    ((BayesNet) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NaiveBayes":
                    cls = new NaiveBayes();
                    ((NaiveBayes) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                // functions
                case "Logistic":
                    cls = new Logistic();
                    ((Logistic) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "MultilayerPerceptron":
                    cls = new MultilayerPerceptron();
                    ((MultilayerPerceptron) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "SimpleLogistic":
                    cls = new SimpleLogistic();
                    ((SimpleLogistic) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "SMO":
                    cls = new SMO();
                    ((SMO) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "VotedPerceptron":
                    cls = new VotedPerceptron();
                    ((VotedPerceptron) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //lazy
                case "IBk":
                    cls = new IBk();
                    ((IBk) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "KStar":
                    cls = new KStar();
                    ((KStar) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "LWL":
                    cls = new LWL();
                    ((LWL) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //meta
                case "AttributeSelectedClassifier":
                    cls = new AttributeSelectedClassifier();
                    ((AttributeSelectedClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ClassificationViaRegression":
                    cls = new ClassificationViaRegression();
                    ((ClassificationViaRegression) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "CostSensitiveClassifier":
                    cls = new CostSensitiveClassifier();
                    ((CostSensitiveClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "FilteredClassifier":
                    cls = new FilteredClassifier();
                    ((FilteredClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Vote":
                    cls = new Vote();
                    ((Vote) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //rules
                case "DecisionTable":
                    cls = new DecisionTable();
                    ((DecisionTable) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "JRip":
                    cls = new JRip();
                    ((JRip) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "OneR":
                    cls = new OneR();
                    ((OneR) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PART":
                    cls = new PART();
                    ((PART) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ZeroR":
                    cls = new ZeroR();
                    ((ZeroR) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //trees
                case "DecisionStump":
                    cls = new DecisionStump();
                    ((DecisionStump) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "J48":
                    cls = new J48();
                    ((J48) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "LMT":
                    cls = new LMT();
                    ((LMT) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "RandomForest":
                    cls = new RandomForest();
                    ((RandomForest) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "REPTree":
                    cls = new REPTree();
                    ((REPTree) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
            }
            if (cls == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cls;
    }

    private static Classifier getDefaultClassifier(String classifierName) {
        Classifier cls = null;
        String options = null;
        try {
            switch (classifierName) {
                // bayes
                case "BayesNet":
                    cls = new BayesNet();
                    options = "-D -Q weka.classifiers.bayes.net.search.local.K2 -- -P 1 -S BAYES -E weka.classifiers.bayes.net.estimate.SimpleEstimator -- -A 0.5";
                    ((BayesNet) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NaiveBayes":
                    cls = new NaiveBayes();
                    break;

                // functions
                case "Logistic":
                    cls = new Logistic();
                    options = "-R 1.0E-8 -M -1";
                    ((Logistic) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "MultilayerPerceptron":
                    cls = new MultilayerPerceptron();
                    options = "-L 0.3 -M 0.2 -N 500 -V 0 -S 0 -E 20 -H a";
                    ((MultilayerPerceptron) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "SimpleLogistic":
                    cls = new SimpleLogistic();
                    options = "-I 0 -M 500 -H 50 -W 0.0";
                    ((SimpleLogistic) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "SMO":
                    cls = new SMO();
                    options = "-C 1.0 -L 0.001 -P 1.0E-12 -N 0 -V -1 -W 1 -K \"weka.classifiers.functions.supportVector.PolyKernel -C 250007 -E 1.0\"";
                    ((SMO) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "VotedPerceptron":
                    cls = new VotedPerceptron();
                    options = "-I 1 -E 1.0 -S 1 -M 10000";
                    ((VotedPerceptron) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //lazy
                case "IBk":
                    cls = new IBk();
                    options = "-K 1 -W 0 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\"";
                    ((IBk) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "KStar":
                    cls = new KStar();
                    options = "-B 20 -M a";
                    ((KStar) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "LWL":
                    cls = new LWL();
                    options = "-U 0 -K -1 -A \"weka.core.neighboursearch.LinearNNSearch -A \\\"weka.core.EuclideanDistance -R first-last\\\"\" -W weka.classifiers.trees.DecisionStump";
                    ((LWL) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //meta
                case "AttributeSelectedClassifier":
                    cls = new AttributeSelectedClassifier();
                    options = "-E \"weka.attributeSelection.CfsSubsetEval \" -S \"weka.attributeSelection.BestFirst -D 1 -N 5\" -W weka.classifiers.trees.J48 -- -C 0.25 -M 2";
                    ((AttributeSelectedClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ClassificationViaRegression":
                    cls = new ClassificationViaRegression();
                    options = "-W weka.classifiers.trees.M5P -- -M 4.0";
                    ((ClassificationViaRegression) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "CostSensitiveClassifier":
                    cls = new CostSensitiveClassifier();
                    options = "-N \"D:\\\\Program Files\\\\Weka-3-6\" -S 1 -W weka.classifiers.rules.ZeroR";
                    ((CostSensitiveClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "FilteredClassifier":
                    cls = new FilteredClassifier();
                    options = "-F \"weka.filters.supervised.attribute.Discretize -R first-last\" -W weka.classifiers.trees.J48 -- -C 0.25 -M 2";
                    ((FilteredClassifier) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Vote":
                    cls = new Vote();
                    options = "-S 1 -B \"weka.classifiers.rules.ZeroR \" -R AVG";
                    ((Vote) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;

                //rules
                case "DecisionTable":
                    cls = new DecisionTable();
                    options = "-X 1 -S \"weka.attributeSelection.BestFirst -D 1 -N 5\"";
                    ((DecisionTable) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "JRip":
                    cls = new JRip();
                    options = "-F 3 -N 2.0 -O 2 -S 1";
                    ((JRip) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "OneR":
                    cls = new OneR();
                    options = "-B 6";
                    ((OneR) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PART":
                    cls = new PART();
                    options = "-M 2 -C 0.25 -Q 1";
                    ((PART) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ZeroR":
                    cls = new ZeroR();
                    break;

                //trees
                case "DecisionStump":
                    cls = new DecisionStump();
                    break;
                case "J48":
                    cls = new J48();
                    options = "-C 0.25 -M 2";
                    ((J48) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "LMT":
                    cls = new LMT();
                    options = "-I -1 -M 15 -W 0.0";
                    ((LMT) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "RandomForest":
                    cls = new RandomForest();
                    options = "-I 100 -K 0 -S 1";
                    ((RandomForest) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "REPTree":
                    cls = new REPTree();
                    options = "-M 2 -V 0.001 -N 3 -S 1 -L -1";
                    ((REPTree) cls).setOptions(weka.core.Utils.splitOptions(options));
                    break;
            }
            if (cls == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cls;
    }

    private void getSVM() {

    }
}
