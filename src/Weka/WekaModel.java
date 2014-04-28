package Weka;

import GUI.Model.Activity;
import GUI.Model.User;
import GUI.Model.UserActivities;
import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.trees.J48;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ArffSaver;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freddy Mesa on 15-Apr-14.
 */
public class WekaModel {
    private Instances trainingSet;
    private Classifier classifier;
    private String path;
    private Instances testingSet;

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

    private void saveTestInstances(Instances testDataSet){
        ArffSaver saver = new ArffSaver();
        String pathFile =  path + "test.arff";

        try {
            saver.setInstances(testDataSet);
            saver.setFile(new File(pathFile));
            saver.writeBatch();
            testDataSet = new Instances(new BufferedReader(new FileReader(pathFile)));
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private Instances convertDataToWekaInstances(List<Data> testData){

        Instances newDataSet = new Instances(trainingSet,testData.size());
        newDataSet.clear();

        for(Data data: testData){
            int  pos = 0;
            Instance newInstance = new DenseInstance(newDataSet.numAttributes());
            newInstance.setDataset(newDataSet);


            for (int i = 0; i < data.binnedAxes.size(); i++){
                for(int j = 0; j < data.binnedAxes.get(i).size(); j++){
                    newInstance.setValue(pos++, data.binnedAxes.get(i).get(j));
                }
            }

            for (int i = 0; i < data.avgAcceleration.size(); i++){
                newInstance.setValue(pos++, data.avgAcceleration.get(i));
            }

            for (int i = 0; i < data.peaks.size(); i++){
                newInstance.setValue(pos++, data.peaks.get(i));
            }

            for (int i = 0; i < data.avgDifAbs.size(); i++){
                newInstance.setValue(pos++, data.avgDifAbs.get(i));
            }

            for (int i = 0; i < data.devStandard.size(); i++){
                newInstance.setValue(pos++, data.devStandard.get(i));
            }

            newInstance.setValue(pos, data.resultant);

            newDataSet.add(newInstance);
        }

        return newDataSet;
    }

    public void startTestingSet(List<Data> outPut){
        Instances testDataSet = convertDataToWekaInstances(outPut);
        final Attribute classAttribute = testDataSet.attribute(testDataSet.numAttributes()-1);
        testDataSet.setClass(classAttribute);

        try {
            for (int i=0; i < testDataSet.numInstances(); i++){
                Instance instance = testDataSet.get(i);

                for(Activity.eActivity activity: Activity.eActivity.values()){

                    instance.setValue(classAttribute, activity.toString());

                    if(activity.equals(Activity.eActivity.Unknown)) break;

                    Evaluation evaluation = new Evaluation(trainingSet);
                    evaluation.evaluateModelOnce(classifier, instance);

                    if(evaluation.correct() == 1) break;
                }
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        setTestSet(testDataSet);
        //saveTestInstances(testDataSet);
    }

    public void setTestSet(Instances testSet) {
        this.testingSet = testSet;
    }

    public List<UserActivities> getUserActivities(User user,List<Activity> activityList){
        List<UserActivities> userActivitiesList = new ArrayList<>();
        final Attribute classAttribute = testingSet.attribute(testingSet.numAttributes()-1);

        for(Instance instance:testingSet){
            UserActivities userActivities = new UserActivities();
            userActivities.setActivityName(instance.stringValue(classAttribute));
            userActivities.setCalorieBurn(userActivities.calculateCaloriesBurn(user,activityList));

            userActivitiesList.add(userActivities);
        }

        return userActivitiesList;
    }

    public static void main (String[] arg){
        Accelerometer output = new Accelerometer(5555);
        output.setTotalSecondsTime(31);
        output.Start();
        WekaModel model = new WekaModel();
        model.startTestingSet(output.getData());
    }
}
