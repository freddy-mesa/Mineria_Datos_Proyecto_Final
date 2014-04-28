package GUI.Model;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.List;

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

    public double calculateCaloriesBurn(User user, List<Activity> activities){
        double resp = 0;

        for (Activity activity:activities){
            if(activityName.equals(activity.getActivityName())){
                if (user.getGenre().equals("Man")){
                    double BasalMetabolicRate = (13.75 * user.getWeight()*0.453592)
                                                +(5 * user.getHeight()*30.48)
                                                -(6.76 * CalendarUtil.getAge(user.getBirthday(),true)) + 66;

                    resp = (BasalMetabolicRate/(24)) * activity.getMetabolicEquivalent() * (10.0/(60*24));
                    break;
                }
                else {
                    double BasalMetabolicRate = (9.56  * user.getWeight()*0.453592)
                            +(1.85 * user.getHeight()*30.48)
                            -(4.68 * CalendarUtil.getAge(user.getBirthday(),true)) + 655;

                    resp = (BasalMetabolicRate/(24)) * activity.getMetabolicEquivalent() * (10.0/(60*24));
                    break;
                }
            }
        }

        return resp;
    }
}
