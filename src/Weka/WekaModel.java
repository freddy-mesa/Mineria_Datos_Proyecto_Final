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

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class WekaModel {
    private Instances trainingSet;
    private Classifier classifier;
    private String path;
    private Instances testingSet;

    /***
     * Constructor de la Clase
     * Carga el archivo de Train y lo instancia en trainingSet
     * Tambien instancia el clasificador J48
     */
    public WekaModel(){
        BufferedReader reader;
        path = System.getProperties().getProperty("user.dir") + "\\src\\Source\\";

        try{
            reader = new BufferedReader(new FileReader(path + "test.arff"));
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

    /***
     * Salva el trainingSet
     * Modulo de ETL (Cargar, Transformar y Predecir)
     */
    private void saveTestInstances(){
        BufferedWriter saver;
        String pathFile =  path + "test.arff";

        try {
            saver = new BufferedWriter(new FileWriter(pathFile));
            saver.write(this.testingSet.toString());
            saver.flush();
            saver.close();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    /***
     * Carga el trainingSet
     */
    private void loadTest(){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(path + "test.arff"));
            testingSet = new Instances(reader);
            testingSet.setClassIndex(testingSet.numAttributes()-1);
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /***
     * Agrega los datos ya preparados a un conjunto de datos (testingSet)
     * Modulo de ETL (Cargar, Transformar y Predecir)
     * @param testData lista de Datos Preparados
     * @return retorna el testingSet
     */
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

    /***
     * Comprueba con el clasificador y determina cual es la actividad correcta
     * Modulo de ETL (Cargar, Transformar y Predecir)
     * @param outPut lista de Datos Preparados
     */
    public void startTestingSet(List<Data> outPut){
        Instances testDataSet = convertDataToWekaInstances(outPut);
        final Attribute classAttribute = testDataSet.attribute(testDataSet.numAttributes()-1);
        testDataSet.setClass(classAttribute);
        List<String> elist = new ArrayList<>();
        List<Double> eListRate = new ArrayList<>();

        try {
            for (int i=0; i < testDataSet.numInstances(); i++){
                Instance instance = testDataSet.get(i);

                for(Activity.eActivity activity: Activity.eActivity.values()){

                    instance.setValue(classAttribute, activity.toString());

                    Evaluation evaluation = new Evaluation(trainingSet);
                    evaluation.evaluateModelOnce(classifier, instance);

                    if(evaluation.correct() == 1){
                        elist.add(activity.toString());
                        eListRate.add(evaluation.pctCorrect());
                    }
                }

                double max = Double.MIN_VALUE;
                int pos = 0;
                for (int j = 0; j < eListRate.size(); ++j){
                    if(eListRate.get(j) > max){
                        max = eListRate.get(j);
                        pos = j;
                    }
                }

                instance.setValue(classAttribute, elist.get(pos));
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        setTestSet(testDataSet);
    }

    /***
     * Permite agregar los datos al trainingSet, añadiendo la clase
     * @param outPut lista de Datos Preparados
     */
    public void populatedTestSet(List<Data> outPut, int type){
        loadTest();
        Instances testDataSet = convertDataToWekaInstances(outPut);
        final Attribute classAttribute = testDataSet.attribute(testDataSet.numAttributes()-1);
        testDataSet.setClass(classAttribute);

        try {
            for (int i=0; i < testDataSet.numInstances(); i++){
                Instance instance = testDataSet.get(i);

                if(type == 1)
                    instance.setValue(classAttribute, Activity.eActivity.Upstairs.toString());
                else
                    instance.setValue(classAttribute, Activity.eActivity.Sitting.toString());
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        addInstancesToTestSet(testDataSet);
        saveTestInstances();
    }

    public void setTestSet(Instances testSet) {
        this.testingSet = testSet;
    }

    public void addInstancesToTestSet(Instances testingSet){
        for (Instance instance:testingSet){
            this.testingSet.add(instance);
        }
    }

    /***
     * Obtiene las lista de Actividades
     * @param user el usuario actual
     * @param activityList lista general de actividades
     * @return la lista de actividades del usuario actual con sus calorias quemadas
     */
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
        output.setTotalSecondsTime(121);
        output.Start();
        WekaModel model = new WekaModel();
        //model.startTestingSet(output.getData());

        model.populatedTestSet(output.getData(),0);
        /*
        model = new WekaModel();
        output.Start();
        model.populatedTestSet(output.getData(),2);*/

    }
}
