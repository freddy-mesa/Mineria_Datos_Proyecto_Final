package GUI.Controller;

import GUI.Model.Activity;
import GUI.Model.User;
import GUI.Model.UserActivities;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freddy Mesa on 28-Apr-14.
 */
public class ChartsController {
    private Stage dialogStage;
    private ObservableList<PieChart.Data> pieChartData;

    @FXML private PieChart pieChart;

    @FXML private void initialize() {

    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void init(User user) {
        pieChartData = FXCollections.observableArrayList();
        List<String> activityName = new ArrayList<>();

        for(Activity.eActivity activity:Activity.eActivity.values()){
            activityName.add(activity.toString());
        }

        int[] activityCount = new int[activityName.size()];

        for(int i=0; i < activityName.size(); ++i){
            for(UserActivities userActivities: user.userActivities){
                if(activityName.get(i).equals(userActivities.getActivityName())){
                    activityCount[i] += 1;
                }
            }
        }

        for(int i=0; i<activityName.size(); ++i){
            if(activityCount[i] != 0)
                pieChartData.add(new PieChart.Data(activityName.get(i),activityCount[i]));
        }

        pieChart.setData(pieChartData);
        pieChart.setTitle("User Activities");
    }

    @FXML private void handleButtonClose() {
        dialogStage.close();
    }
}
