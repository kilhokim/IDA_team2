package structure.weka;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Ethan on 2015-09-10.
 */
public class Experiment {
    public String labelName;
    public ArrayList<String> dataSetNames = new ArrayList<>();
    public ArrayList<Algorithm> algorithms = new ArrayList<>();
    public ArrayList<Result> results = new ArrayList<>();

    public Experiment(String labelName, boolean fileWrite) {
        this.labelName = labelName;
    }

    public void writeTotalResults() {
        String savePath = "results\\";
        String extension = ".csv";
        BufferedWriter bw = null;
        FileWriter fw = null;
        try {
            fw = new FileWriter(savePath + System.currentTimeMillis() + "_" + labelName + "_totalResults" + extension);
            bw = new BufferedWriter(fw);

            bw.write("dataSet," + algorithms.get(0).getHeader() + ",trError,teError,instNum,attNum" + "\n");
            for (int i = 0; i < results.size(); i++) {
                bw.write(results.get(i).toString() + "\n");
            }
            bw.close();
            fw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addResult(String dataSetName, Algorithm algorithm, int order,
                          double trError, double teError, int instanceNum, int attributeNum) {
        this.results.add(new Result(dataSetName, algorithm, order, trError, teError, instanceNum, attributeNum));
    }


    public class Result {
        String dataSetName;
        Algorithm algorithm;
        int order;
        double trError;
        double teError;
        int instanceNum;
        int attributeNum;

        public Result(String dataSetName, Algorithm algorithm, int order,
                      double trError, double teError, int instanceNum, int attributeNum) {
            this.dataSetName = dataSetName;
            this.algorithm = algorithm;
            this.order = order;
            this.trError = trError;
            this.teError = teError;
            this.instanceNum = instanceNum;
            this.attributeNum = attributeNum;
        }

        @Override
        public String toString() {
            String line = dataSetName;
            line += "," + algorithm.printAlgorithm(order);
            line += "," + trError;
            line += "," + teError;
            line += "," + instanceNum;
            line += "," + attributeNum;
            return line;
        }
    }
}
