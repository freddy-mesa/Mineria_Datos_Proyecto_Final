package Weka;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

/**
 * Created by Freddy Mesa on 15-Apr-14.
 */
public class WekaModel {
    private Instances trainingSet;
    private Classifier classifier;
    private String path;

    public enum wekaClass{
        Walking, Jogging, Upstairs, Downstairs, Sitting, Standing
    }

    public WekaModel(){
        BufferedReader reader;
        path = System.getProperties().getProperty("user.dir") + "\\src\\Source\\";

        try{
            reader = new BufferedReader(new FileReader(path + "train.arff"));
            trainingSet = new Instances(reader);
            trainingSet.setClassIndex(trainingSet.numAttributes()-1);

            classifier = new J48();
            classifier.buildClassifier(trainingSet);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private Instances createTestInstances(Accelerometer outPut){

        ArffSaver saver = new ArffSaver();
        String pathFile =  path + "test.arff";
        Instances testDataSet = null;
        try {
            saver.setInstances(convertToInstances(outPut));
            saver.setFile(new File(pathFile));
            saver.writeBatch();

            testDataSet = new Instances(new BufferedReader(new FileReader(pathFile)));
        }
        catch(IOException ex){
            ex.printStackTrace();
        }

        return testDataSet;
    }

    private Instances convertToInstances(Accelerometer testData){

        Instances newDataSet = new Instances(trainingSet,1);
        newDataSet.clear();

        Instance newInstance = new DenseInstance(newDataSet.numAttributes());
        newInstance.setDataset(newDataSet);

        int  pos = 0;

        for (int i = 0; i < testData.binnedAxes.size(); i++){
            for(int j = 0; j < testData.binnedAxes.get(i).size(); j++){
                newInstance.setValue(pos, testData.binnedAxes.get(i).get(j));
                pos++;
            }
        }

        for (int i = 0; i < testData.avgAcceleration.size(); i++){
            newInstance.setValue(pos, testData.avgAcceleration.get(i));
            pos++;
        }

        for (int i = 0; i < testData.peaks.size(); i++){
            newInstance.setValue(pos, testData.peaks.get(i));
            pos++;
        }

        for (int i = 0; i < testData.avgDifAbs.size(); i++){
            newInstance.setValue(pos, testData.avgDifAbs.get(i));
            pos++;
        }

        for (int i = 0; i < testData.devStandard.size(); i++){
            newInstance.setValue(pos, testData.devStandard.get(i));
            pos++;
        }

        newInstance.setValue(pos++, testData.resultant);

        wekaClass clase = wekaClass.Standing;
        newInstance.setValue(pos, clase.toString());

        newDataSet.add(newInstance);

        return newDataSet;
    }

    public void startTestingSet(Accelerometer outPut){
        Instances testDataSet = createTestInstances(outPut);
        testDataSet.setClass(testDataSet.attribute(testDataSet.numAttributes()-1));

        try {
            Evaluation evaluation = new Evaluation(trainingSet);
            evaluation.evaluateModel(classifier, testDataSet);

            System.out.println(evaluation.toSummaryString("\nResults\n======\n", false));
        }
        catch(Exception ex){
            System.out.println(ex.toString());
        }
    }

    public static void main (String[] arg ){
        Accelerometer output = new Accelerometer(5555);
        WekaModel model = new WekaModel();
        model.startTestingSet(output);
    }
}
