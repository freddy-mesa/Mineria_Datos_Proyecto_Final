package GUI;

import GUI.Controller.ActivityController;
import GUI.Model.User;
import GUI.Model.UserActivities;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class GUIApp extends Application {
    public Stage primaryStage;
    public BorderPane rootLayout;
    private ObservableList<User> userData = FXCollections.observableArrayList();
    private ObservableList<UserActivities> userActivitiesData = FXCollections.observableArrayList();
    private User actualUser;

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

        showActivityView();
    }

    public ObservableList<User> getUserData(){
        return userData;
    }
    public ObservableList<UserActivities> getUserActivitiesData(){
        return userActivitiesData;
    }

    public User getActualUser(){
        return actualUser;
    }
    public void addUser(User user){
        this.userData.add(user);
        this.actualUser = user;
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

    public static void main(String[] args) {
        launch(args);
    }
}
