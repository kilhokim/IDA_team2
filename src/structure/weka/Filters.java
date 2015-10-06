package structure.weka;

import weka.filters.AllFilter;
import weka.filters.Filter;
import weka.filters.MultiFilter;
import weka.filters.supervised.attribute.AttributeSelection;
import weka.filters.unsupervised.attribute.*;
import weka.filters.unsupervised.instance.Randomize;

/**
 * Created by Ethan on 2015-09-09.
 */
public class Filters {

    public static Filter getFilter(String fileName, String options, boolean supervised) {
        return getCustomizedFilter(fileName, options, supervised);
    }

    public static Filter getFilter(String fileName, boolean supervised) {
        return getDefaultFilter(fileName, supervised);
    }

    public static MultiFilter getMultiFilter(String[] filterNames, String[] filterOptions, Boolean[] supervised) {
        MultiFilter multiFilter = new MultiFilter();
        Filter[] filters = null;
        if (filterNames == null) {
            filters = new Filter[]{new AllFilter()};
        } else {
            filters = new Filter[filterNames.length];
            for (int i = 0; i < filterNames.length; i++) {
                if (filterOptions != null) filters[i] = Filters.getFilter(filterNames[i], filterOptions[i], supervised[i]);
                else filters[i] = Filters.getFilter(filterNames[i], supervised[i]);
            }
        }
        multiFilter.setFilters(filters);
        return multiFilter;
    }

    private static Filter getCustomizedFilter(String filterName, String options, boolean supervised) {
        if (supervised) return getSupervisedFilter(filterName, options);
        else return getUnsupervisedFilter(filterName, options);
    }

    private static Filter getDefaultFilter(String filterName, boolean supervised) {
        if (supervised) return getSupervisedFilter(filterName);
        else return getUnsupervisedFilter(filterName);
    }

    private static Filter getSupervisedFilter(String filterName, String options) {
        Filter filter = null;
        try {
            switch (filterName) {
                case "AttributeSelection":
                    filter = new AttributeSelection();
                    ((AttributeSelection) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Discretize":
                    filter = new weka.filters.supervised.attribute.Discretize();
                    ((weka.filters.supervised.attribute.Discretize) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NominalToBinary":
                    filter = new weka.filters.supervised.attribute.NominalToBinary();
                    ((weka.filters.supervised.attribute.NominalToBinary) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    private static Filter getSupervisedFilter(String filterName) {
        Filter filter = null;
        String options = null;
        try {
            switch (filterName) {
                case "AttributeSelection":
                    filter = new AttributeSelection();
                    options = "-E \"weka.attributeSelection.CfsSubsetEval \" -S \"weka.attributeSelection.BestFirst -D 1 -N 5\"";
                    ((AttributeSelection) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Discretize":
                    filter = new weka.filters.supervised.attribute.Discretize();
                    options = "-R first-last";
                    ((weka.filters.supervised.attribute.Discretize) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NominalToBinary":
                    filter = new weka.filters.supervised.attribute.NominalToBinary();
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    private static Filter getUnsupervisedFilter(String filterName, String options) {
        Filter filter = null;
        try {
            switch (filterName) {
                case "AddCluster":
                    filter = new AddCluster();
                    ((AddCluster) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ClusterMembership":
                    filter = new ClusterMembership();
                    ((ClusterMembership) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Discretize":
                    filter = new weka.filters.unsupervised.attribute.Discretize();
                    ((weka.filters.unsupervised.attribute.Discretize) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "InterquartileRange":
                    filter = new InterquartileRange();
                    ((InterquartileRange) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NominalToBinary":
                    filter = new weka.filters.unsupervised.attribute.NominalToBinary();
                    ((weka.filters.unsupervised.attribute.NominalToBinary) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Normalize":
                    filter = new Normalize();
                    ((Normalize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NumericToNominal":
                    filter = new NumericToNominal();
                    ((NumericToNominal) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PKIDiscretize":
                    filter = new PKIDiscretize();
                    ((PKIDiscretize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Randomize":
                    filter = new Randomize();
                    ((Randomize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "RemoveUseless":
                    filter = new RemoveUseless();
                    ((RemoveUseless) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Standardize":
                    filter = new Standardize();
                    ((Standardize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "StringToNominal":
                    filter = new StringToNominal();
                    ((StringToNominal) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PrincipalComponents":
                    filter = new PrincipalComponents();
                    ((PrincipalComponents) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
            }
            if (filter == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }

    private static Filter getUnsupervisedFilter(String filterName) {
        Filter filter = null;
        String options = null;
        try {
            switch (filterName) {
                case "AddCluster":
                    filter = new AddCluster();
                    options = "-W \"weka.clusterers.SimpleKMeans -N 2 -A \\\"weka.core.EuclideanDistance -R first-last\\\" -I 500 -S 10\"";
                    ((AddCluster) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "ClusterMembership":
                    filter = new ClusterMembership();
                    options = "-W weka.clusterers.EM -- -I 100 -N -1 -M 1.0E-6 -S 100";
                    ((ClusterMembership) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Discretize":
                    filter = new weka.filters.unsupervised.attribute.Discretize();
                    options = "-B 10 -M -1.0 -R first-last";
                    ((weka.filters.unsupervised.attribute.Discretize) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "InterquartileRange":
                    filter = new InterquartileRange();
                    options = "-R first-last -O 3.0 -E 6.0";
                    ((InterquartileRange) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NominalToBinary":
                    filter = new weka.filters.unsupervised.attribute.NominalToBinary();
                    options = "-R first-last";
                    ((weka.filters.unsupervised.attribute.NominalToBinary) filter)
                            .setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Normalize":
                    filter = new Normalize();
                    options = "-S 1.0 -T 0.0";
                    ((Normalize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "NumericToNominal":
                    filter = new NumericToNominal();
                    options = "-R first-last";
                    ((NumericToNominal) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PKIDiscretize":
                    filter = new PKIDiscretize();
                    options = "-R first-last";
                    ((PKIDiscretize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "PrincipalComponents":
                    filter = new PrincipalComponents();
                    options = "-R 0.95 -A 5 -M -1";
                    ((PrincipalComponents) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Randomize":
                    filter = new Randomize();
                    options = "-S 42";
                    ((Randomize) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "RemoveUseless":
                    filter = new RemoveUseless();
                    options = "-M 99.0";
                    ((RemoveUseless) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
                case "Standardize":
                    filter = new Standardize();
                    break;
                case "StringToNominal":
                    filter = new StringToNominal();
                    options = "-R last";
                    ((StringToNominal) filter).setOptions(weka.core.Utils.splitOptions(options));
                    break;
            }
            if (filter == null) throw new Exception();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return filter;
    }
}
