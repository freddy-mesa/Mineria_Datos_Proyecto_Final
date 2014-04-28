package GUI.Model;

import GUI.GUIApp;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

@XmlRootElement(name = "user")
@XmlType(propOrder = { "name", "genre", "birthday", "height", "weight", "userActivities"})
public class User {
    private String name;
    private String genre;
    private String birthday;
    private double height;
    private double weight;

    @XmlElementWrapper(name = "UserActivitiesList")
    @XmlElement(name = "UserActivities")
    public List<UserActivities> userActivities;

    public String getName(){
        return name;
    }
    public String getGenre(){
        return genre;
    }
    public String getBirthday(){
        return birthday;
    }
    public double getHeight(){
        return height;
    }
    public double getWeight(){
        return weight;
    }

    public void setName(String Name){
        this.name = Name;
    }
    public void setGenre(String Genre){
        this.genre = Genre;
    }
    public void setBirthday(String Birthday){
        this.birthday = Birthday;
    }
    public void setHeight(double Height){
        this.height = Height;
    }
    public void setWeight(double Weight){
        this.weight = Weight;
    }
    public void setUserActivities(List<UserActivities> userActivitiesList){
        this.userActivities = userActivitiesList;
    }

    public void addUserActivity(UserActivities userActivities){
        this.userActivities.add(userActivities);
    }
}
