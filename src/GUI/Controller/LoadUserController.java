package GUI.Controller;

import GUI.GUIApp;
import GUI.Model.CalendarUtil;
import GUI.Model.User;
import GUI.Model.XML_Database;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

/**
 * Created by Freddy Mesa on 28-Apr-14.
 */
public class LoadUserController {
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, String> nameColumn;
    @FXML private TableColumn<User, String> genreColumn;
    @FXML private TableColumn<User, String> birthdayColumn;

    @FXML private Label nameLabel;
    @FXML private Label genreLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label heightLabel;
    @FXML private Label weightLabel;

    private Stage dialogStage;
    private User user;
    private boolean selectedClicked = false;
    private GUIApp guiApp;

    @FXML private void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        genreColumn.setCellValueFactory(new PropertyValueFactory<User, String>("genre"));
        birthdayColumn.setCellValueFactory(new PropertyValueFactory<User, String>("birthday"));

        userTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        showUserDetails(null);

        user = new User();

        userTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<User>() {
            @Override public void changed(ObservableValue<? extends User> observable,User oldValue, User newValue) {
                showUserDetails(newValue);
                user = newValue;
            }
        });
    }

    private void showUserDetails(User user) {
        if(user != null) {
            nameLabel.setText(user.getName());
            genreLabel.setText(user.getGenre());
            birthdayLabel.setText(user.getBirthday());
            heightLabel.setText(String.valueOf(user.getHeight()));
            weightLabel.setText(String.valueOf(user.getWeight()));
        }
        else {
            nameLabel.setText("");
            genreLabel.setText("");
            birthdayLabel.setText("");
            heightLabel.setText("");
            weightLabel.setText("");
        }
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setGUIApp(GUIApp guiApp) {
        this.guiApp = guiApp;

        // Add observable list data to the table
        userTable.setItems(FXCollections.observableArrayList(guiApp.getXML_Db().userList));
    }

    public boolean isSelectClicked() {
        return selectedClicked;
    }

    @FXML private void handleButtonSelect(){
        selectedClicked = true;
        guiApp.setActualUser(user);
        dialogStage.close();
    }

    @FXML private void handleButtonCancel() {
        dialogStage.close();
    }
}
