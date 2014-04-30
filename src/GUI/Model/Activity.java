package GUI.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

@XmlRootElement(name = "Activity")
@XmlType(propOrder = { "activityName", "metabolicEquivalent"})
public class Activity {
    private String activityName;
    private double metabolicEquivalent;
    public static enum eActivity{
        Walking, Jogging, Upstairs, Downstairs, Sitting, Standing
    }

    public Activity(){

    }
    public Activity(String name, double metabolicEquivalent){
        this.setActivityName(name);
        this.setMetabolicEquivalent(metabolicEquivalent);
    }

    public String getActivityName(){
        return activityName;
    }
    public double getMetabolicEquivalent(){
        return metabolicEquivalent;
    }

    public void setMetabolicEquivalent(double metabolicEquivalent){
        this.metabolicEquivalent = metabolicEquivalent;
    }
    public void setActivityName(String activityName){
        this.activityName = activityName;
    }
}
