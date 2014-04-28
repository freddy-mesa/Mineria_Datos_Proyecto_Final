package GUI.Controller;

import GUI.GUIApp;
import GUI.Model.CalendarUtil;
import GUI.Model.User;
import GUI.Model.UserActivities;
import GUI.Model.XML_Database;
import Weka.Accelerometer;
import Weka.WekaModel;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */
public class ActivityController {
    private GUIApp guiApp;
    private AnchorPane pane;
    private int totalTime;
    private XML_Database db;

    @FXML private TextField nameField;
    @FXML private TextField genreField;
    @FXML private TextField birthdayField;
    @FXML private TextField heightField;
    @FXML private TextField weightField;
    @FXML private TextField timeField;

    @FXML private TableView<UserActivities> userActivitiesTableView;
    @FXML private TableColumn<UserActivities, String> activitiesNameColumn;
    @FXML private TableColumn<UserActivities, Double> caloriesBurnColumn;

    @FXML private void initialize(){
        activitiesNameColumn.setCellValueFactory(new PropertyValueFactory<UserActivities, String>("activityName"));
        caloriesBurnColumn.setCellValueFactory(new PropertyValueFactory<UserActivities, Double>("calorieBurn"));
        userActivitiesTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        db = new XML_Database();

        autoWrite();
    }

    public void setMainApp(GUIApp guiApp) {
        this.guiApp = guiApp;
    }
    public GUIApp getMainApp() {
        return guiApp;
    }

    public void setPane(AnchorPane pane) {
        this.pane = pane;
    }
    public AnchorPane getPane() {
        return pane;
    }

    @FXML private void handleButtonValidate(){

    }

    @FXML private void handleButtonStart(){
        if(isInputValid()){
            User user = new User();
            user.setName(nameField.getText());
            user.setGenre(genreField.getText());
            user.setBirthday(birthdayField.getText());
            user.setHeight(Double.parseDouble(heightField.getText()));
            user.setWeight(Double.parseDouble(weightField.getText()));

            guiApp.addUser(user);

            totalTime = Integer.parseInt(timeField.getText());

            StartActivity();
        }
    }

    private void StartActivity(){
        Accelerometer accelerometer = new Accelerometer(5555,totalTime);
        WekaModel wekaModel = new WekaModel();
        wekaModel.startTestingSet(accelerometer.geData());
        User actualUser = guiApp.getActualUser();
        actualUser.setUserActivities(wekaModel.getUserActivities(actualUser, db.activityList));
        userActivitiesTableView.setItems(FXCollections.observableList(actualUser.userActivities));
    }

    private void autoWrite(){
        nameField.setText("Freddy Mesa");
        genreField.setText("Man");
        birthdayField.setText("1992-11-17");
        heightField.setText("5.11");
        weightField.setText("180");
        timeField.setText("31");
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (timeField.getText() == null || timeField.getText().length() == 0) {
            errorMessage += "No valid Name!\n";
        }

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid Name!\n";
        }

        if (!(genreField.getText() == null || genreField.getText().length() == 0)
                && !(genreField.getText().equals("Man") || genreField.getText().equals("Woman"))) {
            errorMessage += "No valid Genre!\n";
        }

        if (birthdayField.getText() == null || birthdayField.getText().length() == 0){
            errorMessage += "No valid Birthday!\n";
        } else {
            if (!CalendarUtil.validString(birthdayField.getText(), true)) {
                errorMessage += "No valid Birthday. Use the format yyyy-mm-dd!\n";
            }
        }

        if (heightField.getText() == null || heightField.getText().length() == 0) {
            errorMessage += "No valid Height!\n";
        } else {
            // try to parse the postal code into an int
            try {
                Double.parseDouble(heightField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Height!\n";
            }
        }

        if (weightField.getText() == null || weightField.getText().length() == 0) {
            errorMessage += "No valid Weight!\n";
        } else {
            // try to parse the postal code into an int
            try {
                Double.parseDouble(weightField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Weight!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            // Show the error message
            Dialogs.showErrorDialog(this.guiApp.primaryStage, errorMessage,
                    "Please correct invalid fields", "Invalid Fields");
            return false;
        }
    }
}
