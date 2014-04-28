package GUI;

import GUI.Controller.ActivityController;
import GUI.Controller.ChartsController;
import GUI.Controller.LoadUserController;
import GUI.Model.User;
import GUI.Model.UserActivities;
import GUI.Model.XML_Database;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUIApp extends Application {
    public Stage primaryStage;
    public BorderPane rootLayout;
    private ObservableList<UserActivities> userActivitiesData;
    private User actualUser;
    private XML_Database db;

    @Override public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        primaryStage.setTitle("Activity Recognition Application ");

        try {
            rootLayout = FXMLLoader.load(getClass().getResource("view/RootLayoutView.fxml"));
            primaryStage.setScene(new Scene(rootLayout));
            primaryStage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }

        db = new XML_Database();
        db.loadDB();

        userActivitiesData = FXCollections.observableArrayList();

        showActivityView();
    }

    public ObservableList<UserActivities> getUserActivitiesData(){
        return userActivitiesData;
    }
    public void setUserActivitiesData(ObservableList<UserActivities> userActivitiesData) {
        this.userActivitiesData = userActivitiesData;
    }

    public User getActualUser(){
        return actualUser;
    }
    public void setActualUser(User user){
        this.actualUser = user;
    }

    public XML_Database getXML_Db(){
        return db;
    }

    public void addUserActivities(ObservableList<UserActivities> userActivitiesData){
        for (UserActivities userActivity:userActivitiesData){
            this.userActivitiesData.add(userActivity);
        }
    }

    public void showActivityView() {
        try {
            // Load the fxml file and set into the center of the main layout
            FXMLLoader loader = new FXMLLoader(GUIApp.class.getResource("view/ActivityView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            rootLayout.setCenter(page);

            // Give the controller access to the main app
            ActivityController controller = loader.getController();

            controller.setMainApp(this);

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public boolean showLoadUser() {
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(GUIApp.class.getResource("view/LoadUserView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Load User");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            // Set the person into the controller
            LoadUserController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setGUIApp(this);

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

            return controller.isSelectClicked();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
            return false;
        }
    }

    public void showCharts(){
        try {
            // Load the fxml file and create a new stage for the popup
            FXMLLoader loader = new FXMLLoader(GUIApp.class.getResource("view/ChartsView.fxml"));
            AnchorPane page = (AnchorPane) loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Charts");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            dialogStage.setScene( new Scene(page));

            // Set the person into the controller
            ChartsController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.init(getActualUser());

            // Show the dialog and wait until the user closes it
            dialogStage.showAndWait();

        } catch (IOException e) {
            // Exception gets thrown if the fxml file could not be loaded
            e.printStackTrace();
        }
    }

    public void save() {
        User savedUser = null;
        for(User user:db.userList){
            if(user.getName().equals(this.actualUser.getName())){
                savedUser = user;
                break;
            }
        }

        if(savedUser == null){
            this.actualUser.addUserActivity(this.getUserActivitiesData());
            db.userList.add(this.actualUser);
        } else{
            savedUser.addUserActivity(this.getUserActivitiesData());
        }

        db.saveDB();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
