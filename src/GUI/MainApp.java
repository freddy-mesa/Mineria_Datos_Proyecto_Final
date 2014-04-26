package GUI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("View/View.fxml"));
            primaryStage.setTitle("Hello World");
            primaryStage.setScene(new Scene(root, 640, 480));
            primaryStage.show();
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
