package GUI.Model;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Freddy Mesa on 27-Apr-14.
 */

@XmlRootElement(namespace = "GUI.Model")
public class XML_Database {

    public static final String databasePath = System.getProperties().getProperty("user.dir") + "\\src\\Source\\db.xml";

    @XmlElementWrapper(name = "ActivityList")
    @XmlElement(name = "Activity")
    public List<Activity> activityList;

    @XmlElementWrapper(name = "UserList")
    @XmlElement(name = "User")
    public List<User> userList;

    public void setActivityList(List<Activity> activityList){
        this.activityList = activityList;
    }
    public void setUserList(List<User> userList){
        this.userList = userList;
    }

    public static void main(String[] args){
        XML_Database db = new XML_Database();

        List<Activity> activities = new ArrayList<>();
        activities.add(new Activity(Activity.eActivity.Walking.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Sitting.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Standing.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Jogging.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Upstairs.toString(),9.0));
        activities.add(new Activity(Activity.eActivity.Downstairs.toString(),9.0));

        db.setActivityList(activities);

        List<User> users = new ArrayList<>();
        User user1 = new User();
        user1.setName("Freddy Mesa");
        user1.setBirthday("1992-11-17");
        user1.setGenre("Men");
        user1.setHeight(5.11);
        user1.setWeight(180);

        List<UserActivities> userActivitiesList = new ArrayList<>();
        userActivitiesList.add(new UserActivities(Activity.eActivity.Walking.toString(), 45.6, "2014-04-27 14:02:23"));
        userActivitiesList.add(new UserActivities(Activity.eActivity.Sitting.toString(), 14.8, "2014-04-27 13:34:56"));
        user1.setUserActivities(userActivitiesList);

        users.add(user1);

        db.setUserList(users);

        try {
            JAXBContext context = JAXBContext.newInstance(XML_Database.class);
            Marshaller m = context.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            // Write to System.out
            m.marshal(db, System.out);

            // Write to File
            m.marshal(db, new File(XML_Database.databasePath));

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }
}
