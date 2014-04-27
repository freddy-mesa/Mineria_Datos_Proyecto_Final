package GUI.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

@XmlRootElement(name = "UserActivities")
@XmlType(propOrder = { "activityName", "calorieBurn", "activityDate"})
public class UserActivities {
    private String activityName;
    private double calorieBurn;
    private String activityDate;

    public UserActivities(){

    }
    public UserActivities(String activityName, double calorieBurn, String activityDate){
        this.activityName = activityName;
        this.calorieBurn = calorieBurn;
        this.activityDate = activityDate;
    }

    public String getActivityName(){
        return activityName;
    }
    public double getCalorieBurn(){
        return calorieBurn;
    }
    public String getActivityDate(){
        return activityDate;
    }

    public void setActivityName(String activity){
        this.activityName = activity;
    }
    public void setCalorieBurn(double calorieBurn){
        this.calorieBurn = calorieBurn;
    }
    public void setActivityDate(String activityDate){
        this.activityDate = activityDate;
    }
}
